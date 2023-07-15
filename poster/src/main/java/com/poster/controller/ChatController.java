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
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable Long chatId){
        return ResponseEntity.ok(chatService.getChatById(chatId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatShortDto>> getChatByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(chatService.getChatByUserId(userId));
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
