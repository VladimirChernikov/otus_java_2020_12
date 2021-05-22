package ru.otus.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.Phone;
import ru.otus.services.ClientService;

@ExtendWith(MockitoExtension.class)
public class ClientRestControllerTest {

    @Mock
    private ClientService clientService;

    private ObjectMapper jsonMapper;
    private MockMvc mvc;
    private List<Client> someClients;

    @BeforeEach
    void setUp() {
        jsonMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(new ClientRestController(clientService)).build();
        someClients = this.generateClients();
    }

    private List<Client> generateClients() {
        var clients = new ArrayList<Client>();
        clients.add( 
                     Client.builder()
                           .name("Client 1")
                           .id(1L)
                           .address( Address.builder().clientId(1L).street("Str 12").build() )
                           .phones( new HashSet<Phone>( 
                                   Arrays.asList( 
                                       Phone.builder().clientId(1L).number("8-123-123-12-12").build() 
                                      ,Phone.builder().clientId(1L).number("7-234-123-12-12").build() 
                                       ) 
                                   ) )
                           .build() 
                   );
        clients.add( 
                     Client.builder()
                           .name("Client 2")
                           .id(2L)
                           .address( Address.builder().clientId(1L).street("Str 13").build() )
                           .phones( new HashSet<Phone>( 
                                   Arrays.asList( 
                                       Phone.builder().clientId(1L).number("8-122-123-12-12").build() 
                                      ,Phone.builder().clientId(1L).number("7-233-123-12-12").build() 
                                       ) 
                                   ) )
                           .build() 
                   );
        return clients;
    }

    @DisplayName("должен возвращать корректного клиента по его id")
    @Test
    void shouldReturnExpectedClientById() throws Exception {
        Client expectedClient = someClients.get(0);

        given(clientService.findById(1L)).willReturn(expectedClient);

        String expectedClientJson = this.jsonMapper.writeValueAsString( expectedClient );

        mvc.perform(get("/api/client/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk() )
                .andExpect( content().string( expectedClientJson ) );
    }

    @DisplayName("должен возвращать корректного клиента по его имени")
    @Test
    void shouldReturnExpectedClientByName() throws Exception {
        List<Client> expectedClient = Arrays.asList( someClients.get(0) );
        String clientName = expectedClient.get(0).getName();

        given(clientService.findByName( clientName )).willReturn(expectedClient);

        String expectedClientJson = this.jsonMapper.writeValueAsString( expectedClient );

        mvc.perform(get("/api/client?name={name}", clientName).accept(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk() )
                .andExpect( content().string( expectedClientJson ) );
    }

    @DisplayName("должен успешно сохранять клиента")
    @Test
    void shouldBeAbleToSaveClient() throws Exception {
        Client expectedClient = someClients.get(0);

        given(clientService.save(expectedClient)).willReturn(expectedClient);

        String expectedClientJson = this.jsonMapper.writeValueAsString( expectedClient );

        mvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedClientJson)
                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect( status().isOk() );
    }

}
