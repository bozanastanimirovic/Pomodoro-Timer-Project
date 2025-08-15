import { Routes } from '@angular/router';
import { HomeComponent } from './components/utility/home/home.component'; 
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ProfileComponent } from './components/utility/profile/profile.component';
import { PomodoroComponent } from './components/main/pomodoro/pomodoro.component';
import { UsersComponent } from './components/main/users/users.component';
import { TeamsComponent } from './components/main/teams/teams.component';
import { StatisticsComponent } from './components/main/statistics/statistics.component';
import { UserTeamsComponent } from './components/main/user-teams/user-teams.component';
import { HomeUserComponent } from './components/utility/home-user/home-user.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent},
  { path: 'pomodoro', component: PomodoroComponent},
  { path: 'users', component: UsersComponent},
  { path: 'teams', component: TeamsComponent},
  { path: 'statistics/:id', component: StatisticsComponent},
  { path: 'userteams', component: UserTeamsComponent},
  { path: 'home-user', component: HomeUserComponent}
];
