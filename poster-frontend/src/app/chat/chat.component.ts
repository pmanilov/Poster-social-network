import { Component, OnInit } from '@angular/core';
import { ChatService } from '../services/chat.service';
import { ChatModel } from '../models/chat.model';
import { MessageModel } from '../models/message.model';
import { interval } from 'rxjs';
import {UserService} from "../services/user.service";
import { map } from 'rxjs/operators';
import {ActivatedRoute} from "@angular/router";
import {UserModel} from "../models/user.model";
import {ChatCreateModel} from "../models/chat.create.model";


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
  currentUserId: number  = 0;
  newUserId: number = 0;
  constructor(private route: ActivatedRoute,
              private chatService: ChatService,
              private userService: UserService) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      if(params.get('userId') != null) {
        this.newUserId = Number(params.get('userId'));
      }
    });
    this.loadUserChats();
      interval(300).subscribe(() => {
        if(this.chatId != 0 || this.newUserId != 0) {
          this.loadChat();
        }
    });
  }

  loadUserChats(): void {
    this.userService.getUserByToken().subscribe(
      (user: UserModel)  => {
        this.currentUser = user.username;
        this.currentUserId = user.id;
        this.loadChatsForUser();
      },
      (error) => {
        console.log('Error loading user:', error);
      }
    );
  }

  loadChatsForUser(): void {
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
    if(this.newUserId != 0) {
      for (let i = 0; i < this.userChats.length; i++) {
        let chat = this.userChats[i];
        if(chat.firstUser.id == this.newUserId || chat.secondUser.id == this.newUserId){
          this.chatId = chat.id;
        }
      }
      if(this.chatId == 0){
        let chatCreate : ChatCreateModel = {
          "firstUserId" : this.currentUserId,
          "secondUserId" : this.newUserId
        }
        this.chatService.createChat(chatCreate).subscribe(
          (createChat: ChatModel) => {
            this.chatId = createChat.id;
            this.loadChatsForUser();
          },(error) => {
            console.log('Chat create error:', error);
          });
          }
      this.newUserId = 0;
      }
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
    //TODO: uncomment
    /*if (this.message.trim() !== '') {
      const newMessage: MessageModel = {
        id: 0,
        date: '',
        text: this.message,
        sender: {id:0, username:'', hasPhoto:false}
      };

      this.chatService.sendMessage(this.chatId, newMessage).subscribe(
        (chat: ChatModel) => {
          this.chat = chat;
          this.message = '';
        },
        (error) => {
          console.log('Error sending message:', error);
        }
      );
    }*/
  }

  selectChat(chatId: number): void {
    this.chatId = chatId;
    this.loadChat();
  }
}
