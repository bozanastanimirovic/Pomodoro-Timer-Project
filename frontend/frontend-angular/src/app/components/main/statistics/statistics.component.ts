import { Component, inject, OnInit } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';
import { AppNavbar } from "../../utility/navbar/navbar.component";
import { TaskService } from '../../../services/task.service';
import { Task } from '../../../models/task';
import { User } from '../../../models/user';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-statistics',
  standalone: true,
  imports: [CommonModule, AppNavbar],
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.scss'
})
export class StatisticsComponent implements OnInit {
  tasks: Task[] = [];
  loading: boolean = true;
  error: string | null = null;
  userName: string = '';
  name: string = '';
  surname: string = '';
  userId: number | null = null;

  constructor(private userService: UserService, private taskService: TaskService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.userId = Number(params['id']);
      if (isNaN(this.userId)) {
        console.error('Invalid userId:', params['id']);
        this.userId = null;
      } else {
        console.log('User ID:', this.userId);
        this.loadUserDetails();
        this.loadUserTasks(); 
      }
    });
  }
  
  loadUserDetails() {
    if (this.userId != null) {
      this.userService.getUserById(this.userId).subscribe({
        next: (user: User) => {
          this.name = user.name;
          this.surname = user.surname;
          this.userName = user.username;
        },
        error: (error) => {
          console.error('Error fetching user details:', error);
          this.error = 'Unable to fetch user details.';
        },
      });
    }
  }

  loadUserTasks() {
    if (this.userId != null) {
      this.taskService.getTasksByUserId(this.userId).subscribe((tasks) => (this.tasks = tasks));
    }
  }
}