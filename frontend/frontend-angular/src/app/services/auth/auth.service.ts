import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { jwtDecode } from 'jwt-decode';
import { NotificationService } from './notification.service';
import { User } from '../../models/user';
import { USER_URL } from '../../constants';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth'

  private userId: number | null = null; 

  constructor(private http: HttpClient, private notificationService: NotificationService ) { }

  login(username: string, password: string): Observable<{ jwtToken: string; refreshToken: string; username: string; roles: string[]; }> {
    const loginData = { username, password };
  
    return this.http.post<{ jwtToken: string; refreshToken: string; username: string; roles: string[]; id: number }>(
      `${this.apiUrl}/public/login`,
      loginData
    )
    .pipe(
      tap(response => {
        if (response.jwtToken) {
          localStorage.setItem('authToken', response.jwtToken); 
          localStorage.setItem('refreshToken', response.refreshToken); 
          localStorage.setItem('username', response.username); 
        } else {
          console.error('JWT Token not found in response:', response);
        }
      }),
      catchError(error => {
        this.notificationService.showMessage('Login failed: Invalid username or password.');
        throw error;
      })
    );
  }

  register(registerRequest: { username: string, email: string, name: string, surname: string, password: string }): Observable<any> { 
    return this.http.post<{ token: string}>(`${this.apiUrl}/public/register`, registerRequest).pipe(
      catchError(error => {
        throw error; 
      })
    );
  }

  refreshToken(): Observable<{ jwtToken: string; refreshToken: string }> {
    const refreshToken = localStorage.getItem('refreshToken');
    
    return this.http.post<{ jwtToken: string; refreshToken: string }>(`${this.apiUrl}/refresh`, { refreshToken })
      .pipe(
        tap(response => {
          if (response.jwtToken) {
            localStorage.setItem('authToken', response.jwtToken); 
            localStorage.setItem('refreshToken', response.refreshToken); 
            console.log('Tokens refreshed');
          }
        }),
        catchError(error => {
          console.error('Refresh token failed:', error);
          throw error;
        })
      );
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  
  isAuthenticated(): boolean {
    return !!localStorage.getItem('authToken'); 
  }

  logout(): void {
    localStorage.removeItem('authToken'); 
  }

  decodeToken(token: string): any {
    return jwtDecode(token);
  }

  

}