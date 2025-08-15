import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { CsrfService } from './csrf.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const jwtHelper = inject(JwtHelperService);
  const csrfService = inject(CsrfService);
  
  const token = localStorage.getItem('authToken');
  const refreshToken = localStorage.getItem('refreshToken');
  
  const csrfToken = localStorage.getItem('X-XSRF-TOKEN');
  
  let clonedRequest = req.clone();

  if (token && !jwtHelper.isTokenExpired(token)) {
    clonedRequest = clonedRequest.clone({
      setHeaders: {
        Authorization: `Bearer ${token}` 
      }
    });
  }

  if (refreshToken) {
    clonedRequest = clonedRequest.clone({
      setHeaders: {
        'Refresh-Token': refreshToken
      }
    });
  }

  if (csrfToken) {
    clonedRequest = clonedRequest.clone({
      setHeaders: {
        'X-XSRF-TOKEN': csrfToken 
      }
    });
  }


  return next(clonedRequest);
};

