import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { Session } from '../../../models/session';
import { SessionType } from '../../../models/enums/sessionType';
import { CommonModule } from '@angular/common';
import { AppNavbar } from '../../utility/navbar/navbar.component';
import { TimerComponent } from '../timer/timer.component';
import { TasksComponent } from '../tasks/tasks.component';
import { Task } from '../../../models/task';

@Component({
  selector: 'app-pomodoro',
  standalone: true,
  imports: [CommonModule, AppNavbar, TimerComponent, TasksComponent],
  templateUrl: './pomodoro.component.html',
  styleUrl: './pomodoro.component.scss'
})
export class PomodoroComponent{

  selectedTask: Task | null = null;

  onTaskSelected(task: Task) {
    this.selectedTask = task;
  }
}