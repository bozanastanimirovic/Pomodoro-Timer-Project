import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TEAM_URL } from '../constants'; 

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  constructor(private httpClient: HttpClient) { }

  getAllTeams(): Observable<any> {
    return this.httpClient.get(`${TEAM_URL}/`);
  }

  getTeamById(id: number): Observable<any> {
    return this.httpClient.get(`${TEAM_URL}/${id}`);
  }

  createTeam(teamName: string): Observable<any> {
    return this.httpClient.post(`${TEAM_URL}/`,  teamName );
  }

  updateTeam(id: number, teamName: string): Observable<any> {
    console.log(teamName);
    return this.httpClient.put(`${TEAM_URL}/${id}`, teamName);
  }

  deleteTeam(id: number): Observable<any> {
    return this.httpClient.delete(`${TEAM_URL}/${id}`, {responseType:"text"});
  }

  getUserTeams(): Observable<any> {
    return this.httpClient.get(`${TEAM_URL}/user`);
  }

}
