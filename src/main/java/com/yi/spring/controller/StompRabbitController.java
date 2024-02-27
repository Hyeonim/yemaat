package com.yi.spring.controller;

import com.yi.spring.entity.ChatDTO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompRabbitController {


    private final RabbitTemplate template;
    public final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(ChatDTO chat, @DestinationVariable String chatRoomId){
//        chat.setMessage("입장하셨습니다.");
        chat.setRegDate(LocalDateTime.now());
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat); // exchange
        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void send(ChatDTO chat, @DestinationVariable String chatRoomId){

        chat.setRegDate(LocalDateTime.now());
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);
        //template.convertAndSend( "room." + chatRoomId, chat);
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat);
    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDTO chat){

//        System.out.println("received : " + chat.getMessage());
    }



    @GetMapping("/procc")
    public ResponseEntity<String> testSendMessage(HttpServletRequest request, @RequestParam("custno") String name ){

        ChatDTO chat = new ChatDTO();
        chat.setNickname( "anomy");
        chat.setMessage( "야이야이야야");

        ServletContext servletContext = request.getServletContext();
        String chatRoomId = (String) servletContext.getAttribute( "user" + name );

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}