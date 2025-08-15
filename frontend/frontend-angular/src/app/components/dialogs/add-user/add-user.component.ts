import { Component, Inject } from '@angular/core';
import { User } from '../../../models/user';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserTeamService } from '../../../services/user-team.service';
import { UserService } from '../../../services/user.service';
import { MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-user',
  standalone: true,
  imports: [
    MatDialogModule, 
    MatListModule, 
    MatCheckboxModule,
    FormsModule,
    CommonModule],
  templateUrl: './add-user.component.html',
  styleUrl: './add-user.component.scss'
})
export class AddUserComponent {
  availableUsers: User[] = [];
  selectedUsers: User[] = [];

  constructor(
    public dialogRef: MatDialogRef<AddUserComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userTeamService: UserTeamService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadAvailableUsers();
  }

  private loadAvailableUsers(): void {
    this.userService.getAllUsers().subscribe((allUsers: User[]) => {
      this.availableUsers = allUsers.filter(
        (user) => !this.data.usersInTeam.some((teamUser: User) => teamUser.id === user.id)
      );
    });
  }

  public addUsers(): void {
    const selectedUsers = this.availableUsers.filter(user => user.selected);
  
    if (selectedUsers.length > 0) {
      const userIds = selectedUsers.map(user => user.id);
      const teamId = this.data.team.id;

      this.userTeamService.addUsersToTeam(teamId, userIds).subscribe(
        (response) => {
          this.dialogRef.close(true);
        },
        (error) => {
          this.dialogRef.close(false);
        }
      );
    } else {
      this.dialogRef.close(false);
    }
  }
  

  public cancel(): void {
    this.dialogRef.close(false); 
  }
}