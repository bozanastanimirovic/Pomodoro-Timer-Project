import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { USER_TEAM_URL } from '../constants'; 
import { User } from '../models/user';  
import { ListDTO } from '../models/DTO/list-dto';

@Injectable({
  providedIn: 'root'
})
export class UserTeamService {

  constructor(private httpClient: HttpClient) { }

  getUsersByTeam(teamId: number): Observable<User[]> {
    return this.httpClient.get<User[]>(`${USER_TEAM_URL}/users/${teamId}`);
  }

  addUsersToTeam(teamId: number, users: number[]): Observable<any> {
    const requestData = new ListDTO(users);
    return this.httpClient.post(`${USER_TEAM_URL}/addUsers/${teamId}`, requestData,  { responseType: 'text' as 'json' });
  }

  addUserToTeam(teamId: number, userId: number): Observable<any> {
    return this.httpClient.put(`${USER_TEAM_URL}/addUser/${teamId}`, userId);
  }

  removeUserFromTeam(teamId: number, userId: number): Observable<any> {
    return this.httpClient.delete(`${USER_TEAM_URL}/deleteUser/${teamId}`, {
      body: userId,  responseType: 'text'
    });
  }
}
