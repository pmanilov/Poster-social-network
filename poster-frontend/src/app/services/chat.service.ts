import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ChatModel } from '../models/chat.model';
import { MessageModel } from '../models/message.model';
import { HttpClient } from '@angular/common/http';
import {AuthService} from "./auth.service";
import {ChatCreateModel} from "../models/chat.create.model";
import {SendMessageModel} from "../models/send-message.model";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  baseUrl = "http://localhost:8080/chats/";

  constructor(
    private httpClient: HttpClient,
    private authService: AuthService
  ) { }


  getChatById(chatId: number) : Observable<ChatModel> {
    return this.httpClient.get<ChatModel>(this.baseUrl+ chatId, this.authService.getAuthorizationHeader())
  }

  sendMessage(chatId:number, message: SendMessageModel): Observable<ChatModel> {
    return this.httpClient.post<ChatModel>(this.baseUrl+ chatId + "/message", JSON.stringify(message), this.authService.getAuthorizationHeaderWithContentType());
  }

  createChat(chat: ChatCreateModel): Observable<ChatModel> {
    return this.httpClient.post<ChatModel>(this.baseUrl + "create", JSON.stringify(chat), this.authService.getAuthorizationHeaderWithContentType());
  }

  getAllChats() : Observable<ChatModel[]> {
    return this.httpClient.get<ChatModel[]>(this.baseUrl, this.authService.getAuthorizationHeader())
  }

}
