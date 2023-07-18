import { Component, OnInit } from '@angular/core';
import { ChatService } from '../services/chat.service';
import { ChatModel } from '../models/chat.model';
import { MessageModel } from '../models/message.model';
import { interval } from 'rxjs';
import {UserService} from "../services/user.service";
import { map } from 'rxjs/operators';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  chatId: number = 0;
  chat: ChatModel | undefined;
  message: string = '';
  userChats: ChatModel[] = [];
  currentUser: string ='';
  constructor(private chatService: ChatService,
              private userService: UserService) { }

  ngOnInit(): void {
    this.loadUserChats();
      interval(500).subscribe(() => {
        if(this.chatId != 0) {
          this.loadChat();
        }
    });
  }

  loadUserChats(): void {
    this.userService.getUserByToken().pipe(
      map(user => user.username)
    ).subscribe(
      (username: string) => {
        this.currentUser = username;
        this.loadChatsForUser(username);
      },
      (error) => {
        console.log('Error loading user:', error);
      }
    );
  }

  loadChatsForUser(username: string): void {
    this.chatService.getAllChats().subscribe(
      (chats: ChatModel[]) => {
        this.userChats = chats.map(chat => ({
          ...chat
        }));
      },
      (error) => {
        console.log('Error loading user chats:', error);
      }
    );
  }
  getOtherUserName(chat: ChatModel, currentUser: string): string {
    if (chat.firstUser.username === currentUser) {
      return chat.secondUser.username;
    } else {
      return chat.firstUser.username;
    }
  }

  loadChat(): void {
    this.chatService.getChatById(this.chatId).subscribe(
      (currentChat: ChatModel) => {
        this.chat = currentChat;
      },
      (error) => {
        console.log('Chat loading error:', error);
      }
    );
  }
  sendMessage(): void {
    if (this.message.trim() !== '') {
      const newMessage: MessageModel = {
        id: 0, // Используйте нужное значение для id сообщения
        date: '', // Получение текущей даты и времени
        text: this.message,
        sender: {id:0, username:''}
      };

      this.chatService.sendMessage(this.chatId, newMessage).subscribe(
        (chat: ChatModel) => {
          this.chat = chat;
          this.message = ''; // Очистка поля ввода сообщения после отправки
        },
        (error) => {
          console.log('Error sending message:', error);
        }
      );
    }
  }

  selectChat(chatId: number): void {
    this.chatId = chatId;
    this.loadChat();
  }
}
