package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.util.HtmlUtils;

import ru.otus.domain.Client;
import ru.otus.dto.ClientMessageData;
import ru.otus.front.FrontendService;

@Controller
public class ClientWsController {
    private static final Logger logger = LoggerFactory.getLogger(ClientWsController.class);

    private final FrontendService frontendService;
    private final SimpMessagingTemplate messagingTemplate;

    public ClientWsController(
            FrontendService frontendService,
            SimpMessagingTemplate messagingTemplate
            ) {
        this.frontendService = frontendService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/client.{id}")
    // @SendTo("/topic/status/{sessionId}")
    public void getClientById( @Header("sessionId") String sessionId,  @DestinationVariable long id) {
       
        String outputTopicStatus = "/topic/status/"+sessionId;
        String outputTopic = "/topic/clients/"+sessionId;

        try {
            frontendService.getClientById(id, 
                    clientMessageData -> { 
                        if ( clientMessageData.getStatus().equals( ClientMessageData.STATUS_OK ) )  {
                            messagingTemplate.convertAndSend(outputTopicStatus, clientMessageData.getClient()); 
                            messagingTemplate.convertAndSend(outputTopic, clientMessageData.getClient()); 
                        }
                        else  {
                            messagingTemplate.convertAndSend(outputTopicStatus, 
                                    String.format("Ошибка при запросе клиента %d: %s", id, clientMessageData.getErrorMessage() )
                                    ); 
                        }
                    });
            messagingTemplate.convertAndSend(outputTopicStatus, String.format("Запрос клиента c id = %d отправлен...", id ));

        } catch (Exception e){
            messagingTemplate.convertAndSend(outputTopicStatus, String.format("An error occured. Please contact your system administrator."));
            throw e;
        }
    }

    @MessageMapping("/client")
    // @SendTo("/topic/status/{sessionId}")
    public void putClient(@Header("sessionId") String sessionId, @RequestBody Client client) {

        String outputTopicStatus = "/topic/status/"+sessionId;
        String outputTopicAll = "/topic/clients";

        try {
            frontendService.putClient(client, 
                    clientMessageData -> { 
                        if ( clientMessageData.getStatus().equals( ClientMessageData.STATUS_OK ) )  {
                            messagingTemplate.convertAndSend(outputTopicStatus, clientMessageData.getClient()); 
                            messagingTemplate.convertAndSend(outputTopicAll, clientMessageData.getClient()); 
                        }
                        else  {
                            messagingTemplate.convertAndSend(outputTopicStatus, 
                                    String.format("Ошибка при добалении клиента %s: %s", clientMessageData.getClient().getName(), clientMessageData.getErrorMessage() )
                                    ); 
                        }
            });
            messagingTemplate.convertAndSend(outputTopicStatus, String.format("Запрос на добавление клиента %s отправлен...", client.getName()));

        } catch (Exception e){
            messagingTemplate.convertAndSend(outputTopicStatus, String.format("An error occured. Please contact your system administrator."));
            throw e;
        }

    }

}
