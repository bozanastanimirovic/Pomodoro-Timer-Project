import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { AppNavbar } from './components/utility/navbar/navbar.component';
import { CsrfService } from './services/auth/csrf.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Angular Frontend'
  
  constructor(private csrfService: CsrfService) {}

  ngOnInit(): void {
    this.csrfService.initializeCsrfToken();
  }
}
