import { Component } from "@angular/core";
import { AuthService } from "../../../services/auth/auth.service";
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { CommonModule } from "@angular/common";
import { Router } from "@angular/router";
import { MatSnackBarModule } from '@angular/material/snack-bar'; 
import { RouterModule } from "@angular/router";
import { NotificationService } from "../../../services/auth/notification.service";
import { UserService } from "../../../services/user.service";
import { User } from "../../../models/user";
import { UserRole } from "../../../models/enums/userRole";


@Component({
    selector: 'app-login',
    standalone: true, 
    imports: [FormsModule, CommonModule, MatButtonModule, MatInputModule, FormsModule, MatSnackBarModule, RouterModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {
    username: string = '';
    password: string = '';
    loginError: boolean = false;
    role : UserRole | undefined;

    constructor(
        private authService: AuthService,
        private userService: UserService,
        private router: Router){}

    onLogin(){
        this.authService.login(this.username, this.password).subscribe(
            () => {

                this.userService.getAuthenticatedUser().subscribe({
                    next: (data: User) => {
                        if (data.role.roleName === UserRole.RoleAdmin) {
                            this.router.navigate(['/home']); 
                        } else {
                            this.router.navigate(['/home-user']);
                        }
                    },
                    error: (error) => {
                      console.error('Error fetching authenticated user:', error);
                    },
                  });
                
                console.log(this.role);

                
            },
            error => {
                console.error('Login error: ', error);
                this.loginError = true;
            }
        );
    }


}