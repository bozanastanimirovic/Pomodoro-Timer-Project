import { CommonModule } from '@angular/common';
import { Component, Input, SimpleChanges } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { Task } from '../../../models/task';
import { User } from '../../../models/user';
import { UserService } from '../../../services/user.service';
import { SessionType } from '../../../models/enums/sessionType';

@Component({
  selector: 'app-timer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './timer.component.html',
  styleUrl: './timer.component.scss'
})
export class TimerComponent {
  @Input() selectedTask: Task | null = null; 
  sessionType: SessionType = SessionType.POMODORO;  running = false;
  minutes = 0;
  seconds = 25;
  private timerInterval: any;
  SessionType = SessionType;
  isPaused: boolean = false;
  completedPomodoros = 0;
  

  session: any = null; 
  currentUser: User | null = null; 

  constructor(
    private sessionService: SessionService,
    private userService: UserService
  ) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['selectedTask'] && changes['selectedTask'].currentValue) {
      console.log('Selected task changed:', this.selectedTask);
    } else {
      console.log('No task selected');
    }
  }


  ngOnInit() {
    this.userService.getAuthenticatedUser().subscribe({
      next: (user: User) => {
        this.currentUser = user;
      },
      error: (err) => console.error('Error fetching user:', err),
    });
  }

  setSession(type: SessionType) {
    this.sessionType = type;
    if (type === SessionType.POMODORO) {
      this.seconds = 25;
    } else if (type === SessionType.SHORT_BREAK) {
      this.seconds = 5;
    } else if (type === SessionType.LONG_BREAK) {
      this.seconds = 15;
    }
    this.running = false;
    clearInterval(this.timerInterval);
  }

  startSession() {
    if (!this.selectedTask) {
      console.error('No task selected for the session.');
      return;
    }
  
    if (!this.currentUser) {
      console.error('User not authenticated.');
      return;
    }

    if (this.running && !this.isPaused) {
      this.pauseSession();
      return;
    }
  
    console.log('Starting session for task:', this.selectedTask);
  
    if (!this.session) {
      this.sessionService.createSession(this.sessionType).subscribe({
        next: (session) => {
          this.session = session;
          this.running = true;
          this.isPaused = false;
          this.startTimerCountdown(); 
          console.log('Session started successfully:', session);
        },
        error: (err) => console.error('Error starting session:', err),
      });
    } else {
      this.running = true;
      this.isPaused = false;
      this.startTimerCountdown();
    }
  }
  
  startTimerCountdown() {
    console.log('Timer countdown started');
    this.timerInterval = setInterval(() => {
      if (this.seconds === 0) {
        if (this.minutes === 0) {
          this.running = false;
          clearInterval(this.timerInterval);
          this.completeCurrentSession(); 
        } else {
          this.minutes--;
          this.seconds = 59;
        }
      } else {
        this.seconds--;
      }
    }, 1000);
  }

  completeCurrentSession() {
    console.log(`Completed session: ${this.sessionType}`);

    if (this.sessionType === SessionType.POMODORO) {
      this.completedPomodoros++;

      if (this.completedPomodoros % 4 === 0) {
        this.setSession(SessionType.LONG_BREAK);
      } else {
        this.setSession(SessionType.SHORT_BREAK);
      }
    } else {
      this.setSession(SessionType.POMODORO);
    }

    this.finishSession();
  }

  finishSession() {
    if (!this.session || !this.selectedTask || !this.currentUser) {
      console.error('Cannot finish session. Missing session, task, or user information.');
      return;
    }

    this.sessionService.finishSession(this.session.id, this.selectedTask.id).subscribe({
      next: (newSession) => {
        console.log('Session finished successfully for task:', this.selectedTask?.name);
        this.session = newSession; 
        this.resetTimer();
      },
      error: (err) => console.error('Error finishing session:', err),
    });
  }

  resetTimer() {
    this.running = false; 
    this.isPaused = false;
    this.setSession(this.sessionType);
    clearInterval(this.timerInterval); 
  }

  pauseSession() {
    if (!this.session || !this.running) {
      console.error('No active session to pause.');
      return;
    }
  
    this.isPaused = true;
    this.running = false;
  
    clearInterval(this.timerInterval); 
  
    const timeLeft = this.minutes * 60 + this.seconds;
    this.sessionService.toggleSessionPause(this.session.id, timeLeft).subscribe({
      next: (updatedSession) => {
        console.log('Session paused successfully:', updatedSession);
        this.session = updatedSession;
      },
      error: (err) => console.error('Error pausing session:', err),
    });
  }

  resumeSession() {
    if (this.isPaused) {
      this.running = true;
      this.isPaused = false;
      this.startTimerCountdown();
      console.log('Resuming session...');
    }
  }
  


  ngOnDestroy() {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }
}
