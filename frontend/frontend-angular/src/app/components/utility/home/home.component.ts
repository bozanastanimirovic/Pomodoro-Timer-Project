import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';   
import { UserService } from '../../../services/user.service'; 
import { User } from '../../../models/user';  
import { MatCardModule } from '@angular/material/card'; 
import { MatButtonModule } from '@angular/material/button'; 
import { CommonModule } from '@angular/common'; 
import { MatSnackBarModule } from '@angular/material/snack-bar';  
import { RouterModule } from '@angular/router';  
import { AppNavbar } from '../navbar/navbar.component';


@Component({
  selector: 'app-home',
  standalone: true,
  imports:  [MatButtonModule, RouterModule, AppNavbar, MatSnackBarModule,MatCardModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  username: string = '';
  totalUsers: number = 0;
  activeSessions: number = 0;
  currentUser: User | null = null; 

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit() {
    this.userService.getAuthenticatedUser().subscribe({
      next: (user: User) => {
        this.currentUser = user;
      },
      error: (err) => console.error('Error fetching user:', err),
    });
  }


}

