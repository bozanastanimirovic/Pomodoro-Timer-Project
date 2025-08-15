import { Component, EventEmitter, Output } from '@angular/core';
import { TaskService } from '../../../services/task.service';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user';
import { Task } from '../../../models/task';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatListModule,
    MatIconModule
  ],
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss'],
})
export class TasksComponent {
  tasks: Task[] = [];
  showForm = false;
  taskName = '';
  @Output() taskSelected = new EventEmitter<Task>();
  selectedTask: Task | null = null;

  userId: number | null = null;

  constructor(private taskService: TaskService, private userService: UserService) {}

  ngOnInit() {
    this.userService.getAuthenticatedUser().subscribe({
      next: (data: User) => {
        this.userId = data.id;
        this.loadUserTasks();
      },
      error: (error) => {
        console.error('Error fetching authenticated user:', error);
      },
    });
  }

  loadUserTasks() {
    if (this.userId != null) {
      this.taskService.getTasksByUserId(this.userId).subscribe((tasks) => {
        console.log('Fetched tasks:', tasks);
        this.tasks = tasks.filter((task: Task) => !task.finished);
        console.log('Filtered tasks:', this.tasks);
      });
    }
  }

  selectTask(task: Task) {
    this.selectedTask = task;
    this.taskSelected.emit(task); 
  }

  addTask(taskName: string) {
    this.taskService.createTask({ name: taskName, teamName: 'Default' }).subscribe(() => this.loadUserTasks());
  }

  toggleTaskFinished(task: Task) {
    this.taskService.toggleFinishedTask(task.id).subscribe(() => this.loadUserTasks());
    console.log(task.finished);
  }

  deleteTask(taskId: number) {
    this.taskService.deleteTask(taskId).subscribe(() => this.loadUserTasks());
  }

  toggleForm() {
    this.showForm = !this.showForm;
    if (!this.showForm) this.taskName = '';
  }
}
