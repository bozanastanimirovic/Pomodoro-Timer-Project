import { Component } from "@angular/core";
import { AuthService } from "../../../services/auth/auth.service";
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NotificationService } from "../../../services/auth/notification.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    RouterModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  username: string = '';
  email: string = '';
  firstName: string = '';
  lastName: string = '';
  password: string = '';
  confirmPassword: string = '';
  registerError: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService 
  ) {}

  onRegister() {
    if (this.password !== this.confirmPassword) {
      this.registerError = "Passwords do not match!";
      return;
    }
  
    const registerRequest = {
      username: this.username,
      email: this.email,
      name: this.firstName, 
      surname: this.lastName, 
      password: this.password,
    };
  
    this.authService.register(registerRequest).subscribe(
      (response) => {
        console.log('Registration successful');
      console.log('Navigating to login...');
        this.router.navigate(['/login']);
      },
      (error) => {
        if (error.status === 400) {
          if (error.error === "Error: Username is already taken!") {
            this.registerError = "This username is already taken. Please choose another one.";
            this.notificationService.showMessage(this.registerError);
          } else if (error.error === "Error: Email is already in use!") {
            this.registerError = "This email is already in use. Please choose another one.";
            this.notificationService.showMessage(this.registerError);
          } else {
            this.registerError = "An error occurred during registration. Please try again later.";
            this.notificationService.showMessage(this.registerError);
          }
        } else {
          this.registerError = "An unexpected error occurred.";
          this.notificationService.showMessage(this.registerError);
        }
        console.error('Registration error: ', error);
    }
    );
  }
}
