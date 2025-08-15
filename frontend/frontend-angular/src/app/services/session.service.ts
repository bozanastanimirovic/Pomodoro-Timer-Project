import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SESSION_URL } from '../constants'; 
import { Session } from '../models/session';   
import { SessionType } from '../models/enums/sessionType';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
    apiUrl = "http://localhost:8080/api/";

  constructor(private httpClient: HttpClient) { }

  createSession(sessionType: SessionType): Observable<Session> {
    return this.httpClient.post<Session>(`${SESSION_URL}/`, sessionType);
  }

  toggleSessionPause(id: number, timeLeft: number): Observable<Session> {
    return this.httpClient.put<Session>(`${SESSION_URL}/toggle-pause/${id}`, timeLeft);
  }

  finishSession(id: number, taskId: number): Observable<Session> {
    return this.httpClient.put<Session>(`${SESSION_URL}/finish-session/${id}`,taskId);
  }
}
