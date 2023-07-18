package com.poster.controller;

import com.poster.dto.ChatDto;
import com.poster.dto.ChatShortDto;
import com.poster.model.Chat;
import com.poster.model.ChatRequest;
import com.poster.model.Message;
import com.poster.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Authorization"})
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable Long chatId){
        return ResponseEntity.ok(chatService.getChatById(chatId));
    }

    @GetMapping("/")
    public ResponseEntity<List<ChatShortDto>> getAllChats(){
        return ResponseEntity.ok(chatService.getAllChats());
    }

    @PostMapping("/create")
    public ResponseEntity<ChatDto> createChat(@RequestBody ChatRequest chatRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(chatRequest));
    }

    @PostMapping("{chatId}/message")
    public ResponseEntity<ChatDto> addMessage(@PathVariable Long chatId, @RequestBody Message message){
        return ResponseEntity.ok(chatService.addMessage(chatId, message));
    }
}
