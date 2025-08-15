import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

export const csrfInterceptor: HttpInterceptorFn = (req, next) => {
 const csrfToken = getCookie('XSRF-TOKEN');
  console.log('CSRF Token from cookie:', csrfToken);

  const clonedRequest = csrfToken
    ? req.clone({
        setHeaders: {
          'X-XSRF-TOKEN': csrfToken, 
        },
      })
    : req;
  
  console.log('Request headers:', clonedRequest.headers);

  return next(clonedRequest);
};

function getCookie(name: string): string | null {
  const matches = document.cookie.match(new RegExp(`(?:^|; )${name}=([^;]*)`));
  return matches ? decodeURIComponent(matches[1]) : null;
}

