package com.poster.service;

import ch.qos.logback.core.util.CachingDateFormatter;
import com.poster.dto.ChatDto;
import com.poster.dto.ChatShortDto;
import com.poster.dto.MessageDto;
import com.poster.dto.UserShortInfo;
import com.poster.exception.ChatAlreadyCreated;
import com.poster.exception.ChatNotFoundException;
import com.poster.exception.UserActionRestrictedException;
import com.poster.model.Chat;
import com.poster.model.ChatRequest;
import com.poster.model.Message;
import com.poster.model.User;
import com.poster.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public ChatDto getChatById(Long chatId){
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if(chatOptional.isPresent()){
            Chat chat = chatOptional.get();
            if(chat.getFirstUser().getId().equals(userService.getAuthorizedUser().getId())
                    || chat.getSecondUser().getId().equals(userService.getAuthorizedUser().getId())) {
                return this.convertChatToDto(chat);
            }
            else {
                throw new UserActionRestrictedException("Access to the requested action is restricted.");
            }
        }
        else {
            throw new ChatNotFoundException("The chat with ID "+chatId+" does not exist!");
        }
    }

    public List<ChatShortDto> getAllChats() {
        return chatRepository.findAllByUserId(userService.getAuthorizedUser().getId()).stream().map(this::convertChatToShortDto).collect(Collectors.toList());
    }

    public ChatDto createChat(ChatRequest chatRequest) {
        User firstUser = userService.getUserEntityById(chatRequest.getFirstUserId());
        User secondUser = userService.getUserEntityById(chatRequest.getSecondUserId());
        if(!firstUser.getId().equals(userService.getAuthorizedUser().getId()) && !secondUser.getId().equals(userService.getAuthorizedUser().getId())){
            throw new UserActionRestrictedException("Access to the requested action is restricted.");
        }
        if (chatRepository.findByUsersId(firstUser.getId(), secondUser.getId()).isEmpty()) {
            Chat chat = new Chat();
            chat.setFirstUser(firstUser);
            chat.setSecondUser(secondUser);
            return this.convertChatToDto(chatRepository.save(chat));
        }
        else throw new ChatAlreadyCreated("Chat is already created");
    }

    public ChatDto addMessage(Long chatId, Message message) {
        message.setSender(userService.getAuthorizedUser());
        message.setDate(LocalDateTime.now());
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if(chatOptional.isPresent()){
            Chat chat = chatOptional.get();
            if(chat.getFirstUser().getId().equals(userService.getAuthorizedUser().getId())
                    || chat.getSecondUser().getId().equals(userService.getAuthorizedUser().getId())) {
                message.setChat(chat);
                chat.getMessages().add(message);
                return  this.convertChatToDto(chatRepository.save(chat));
            }
            else {
                throw new UserActionRestrictedException("Access to the requested action is restricted.");
            }
        }
        else {
            throw new ChatNotFoundException("The chat with ID "+message.getChat().getId()+" does not exist!");
        }
    }

    private ChatDto convertChatToDto(Chat chat){
        return ChatDto.builder()
                .id(chat.getId())
                .firstUser(convertUserToShortInfo(chat.getFirstUser()))
                .secondUser(convertUserToShortInfo(chat.getSecondUser()))
                .messages(chat.getMessages().stream().map(this::convertMessageToDto).collect(Collectors.toList()))
                .build();
    }

    private ChatShortDto convertChatToShortDto(Chat chat){
        return ChatShortDto.builder()
                .id(chat.getId())
                .firstUser(convertUserToShortInfo(chat.getFirstUser()))
                .secondUser(convertUserToShortInfo(chat.getSecondUser()))
                .build();
    }

    private MessageDto convertMessageToDto(Message message){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return  MessageDto.builder()
                .id(message.getId())
                .date(message.getDate().format(formatter))
                .text(message.getText())
                .sender(convertUserToShortInfo(message.getSender()))
                .build();
    }

    private UserShortInfo convertUserToShortInfo(User user){
        return UserShortInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
