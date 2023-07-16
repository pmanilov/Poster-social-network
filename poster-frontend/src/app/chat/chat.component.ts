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
    this.userService.getUser().pipe(
      map(user => user.username)
    ).subscribe(
      (username: string) => {
        this.currentUser = username;
        this.loadChatsForUser(username);
      },
      (error) => {
        console.log('Ошибка при загрузке пользователя:', error);
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
        console.log('Ошибка при загрузке чатов пользователя:', error);
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
        this.formatMessageDates();
      },
      (error) => {
        console.log('Ошибка при загрузке чата:', error);
      }
    );
  }
  formatMessageDates(): void {
    if (this.chat && this.chat.messages) {
      this.chat.messages.forEach((message: MessageModel) => {
        message.date = this.formatDate(message.date); // Форматируем дату каждого сообщения
      });
    }
  }

  formatDate(dateString: string): string {
    const date: Date = new Date(dateString);
    const options: Intl.DateTimeFormatOptions = {
      year: '2-digit',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    };
    return date.toLocaleDateString('ru-RU', options).replace(',', '');
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
          console.log('Ошибка при отправке сообщения:', error);
        }
      );
    }
  }

  selectChat(chatId: number): void {
    this.chatId = chatId;
    this.loadChat();
  }
}
