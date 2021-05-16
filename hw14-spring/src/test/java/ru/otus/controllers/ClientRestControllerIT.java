package ru.otus.controllers;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.otus.domain.Client;
import ru.otus.services.ClientService;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientRestControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientService clientService;

    private ObjectMapper jsonMapper;
    private List<Client> someClients;

    @BeforeEach
    void setUp() {
        jsonMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(new ClientRestController(clientService)).build();
        someClients = this.generateClients();
    }

    private List<Client> generateClients() {
        var clients = new ArrayList<Client>();
        clients.addAll( clientService.findAll() );
        return clients;
    }

    @DisplayName("должен возвращать корректного клиента по его id")
    @Test
    void shouldReturnExpectedClientById() throws Exception {
        Client expectedClient = someClients.get(0);

        String expectedClientJson = this.jsonMapper.writeValueAsString( expectedClient );

        mvc.perform(get("/api/client/{id}", expectedClient.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk() )
                .andExpect( content().string( expectedClientJson ) );
    }

    @DisplayName("должен возвращать корректного клиента по его имени")
    @Test
    void shouldReturnExpectedClientByName() throws Exception {
        List<Client> expectedClient = Arrays.asList( someClients.get(0) );
        String clientName = expectedClient.get(0).getName();

        String expectedClientJson = this.jsonMapper.writeValueAsString( expectedClient );

        mvc.perform(get("/api/client?name={name}", clientName).accept(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk() )
                .andExpect( content().string( expectedClientJson ) );
    }

    @DisplayName("должен успешно сохранять клиента")
    @Test
    void shouldBeAbleToSaveClient() throws Exception {
        Client expectedClient = someClients.get(0);

        String expectedClientJson = this.jsonMapper.writeValueAsString( expectedClient );

        mvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedClientJson)
                .accept(MediaType.APPLICATION_JSON)
                )
                // .andExpect( content().string("SHOWME") )
                .andExpect( status().isOk() )
                ;
    }


    @DisplayName("скорость работы с клиентами выше 100 сохранений в секунду")
    @Test
    void shouldHandleClientsFast() {
        assertTimeout(Duration.ofSeconds(5), () -> 
                {
                    int count = 0;
                    while ( count < 500 ){
                        for (var client : someClients ) {
                            clientService.save(client);
                            count++;
                        }
                    }
                }
                );
    }

}
