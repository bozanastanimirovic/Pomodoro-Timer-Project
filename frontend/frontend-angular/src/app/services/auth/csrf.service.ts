import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CsrfService {
    private apiUrl = 'http://localhost:8080'
  private csrfUrl = `${this.apiUrl}/api/csrf-token`; 

  constructor(private http: HttpClient) {}

  initializeCsrfToken(): void {
    if (!this.getCookie('XSRF-TOKEN')) {
      this.http.get<{ token: string }>(this.csrfUrl, { withCredentials: true }).subscribe({
        next: (response) => {
          const csrfToken = response.token;
          
          localStorage.setItem('X-XSRF-TOKEN', csrfToken);
          this.setCookie('XSRF-TOKEN', csrfToken, 1);
        },
        error: (err) => {
          console.error('Greška prilikom preuzimanja CSRF tokena:', err);
        }
      });
    }
  }

  public getCookie(name: string): string | null {
    const matches = document.cookie.match(new RegExp(`(?:^|; )${name}=([^;]*)`));
    return matches ? decodeURIComponent(matches[1]) : null;
  }

  private setCookie(name: string, value: string, days: number): void {
    const expires = new Date();
    expires.setTime(expires.getTime() + (days * 24 * 60 * 60 * 1000));
    document.cookie = `${name}=${encodeURIComponent(value)}; expires=${expires.toUTCString()}; path=/; secure; samesite=strict`;
  }

  removeCsrfToken(): void {
    document.cookie = 'XSRF-TOKEN=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; secure; samesite=strict';
  }
}
