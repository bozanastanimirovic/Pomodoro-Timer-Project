import { Component, ViewChild } from "@angular/core";
import { Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from "../../../services/auth/auth.service";
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { UserService } from "../../../services/user.service";
import { User } from "../../../models/user";
import { UserRole } from "../../../models/enums/userRole";

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [
      MatButtonModule, 
      MatToolbarModule, 
      MatIconModule,
      MatSidenavModule,
      MatListModule,
      CommonModule, 
      RouterModule],
    templateUrl:"./navbar.component.html",
    styleUrl: './navbar.component.scss'
})
export class AppNavbar {

  user: User | null = null;
  UserRole = UserRole;
  drawerOpened = false;
  id: number | null = null;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.userService.getAuthenticatedUser().subscribe(
        (data: User) => {
          this.user = data;
          this.id = data.id;
        },
        (error) => {
          console.error('Error fetching authenticated user:', error);
        }
      );
    }
  }

  toggleSidebar(): void {
    this.drawerOpened = !this.drawerOpened;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['']);
  }
}