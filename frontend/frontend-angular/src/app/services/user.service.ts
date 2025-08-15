import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { USER_URL } from '../constants';
import { User } from '../models/user';
import { UserRole } from '../models/enums/userRole';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient:HttpClient, private authService: AuthService) { }

  private getHeaders() {
  }

  public getAllUsers():Observable<any>{
    return this.httpClient.get(`${USER_URL}/manager`)
  }
  public getUserById(userId:number):Observable<any>{
    return this.httpClient.get(`${USER_URL}/manager/${userId}`)
  }

  updateUserRole( role: UserRole, id: number): Observable<User> {
    const headers = this.getHeaders();
    return this.httpClient.put<User>(`${USER_URL}/manager/${id}`, role);
  }

  deleteUserManager(id: number): Observable<any> {
    return this.httpClient.delete(`${USER_URL}/manager/${id}`, {responseType:"text"});
  }

  getUserByName(name: string): Observable<any> {
    return this.httpClient.post<User>(`${USER_URL}/manager/name`, name);
  }

  getAuthenticatedUser(): Observable<User> {
    return this.httpClient.get<User>(`${USER_URL}/`);
  }

  updateUser(id: number, user: User): Observable<User> {
    return this.httpClient.put<User>(`${USER_URL}/${id}`, user);
  }

  deleteUser(): Observable<any> {
    return this.httpClient.delete(`${USER_URL}`);
  }

  getUserStatistics(): Observable<any> {
    return this.httpClient.get(`${USER_URL}/statistics`); 
  }
}