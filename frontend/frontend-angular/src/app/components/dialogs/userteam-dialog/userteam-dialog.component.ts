import { Component, Inject } from '@angular/core';
import { Team } from '../../../models/team';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from '../../../models/user';
import { TeamUsersComponent } from '../../main/team-users/team-users.component';
import { UserTeamService } from '../../../services/user-team.service';
import { TeamService } from '../../../services/team.service';
import { MatDialogModule } from '@angular/material/dialog';


@Component({
  selector: 'app-userteam-dialog',
  standalone: true,
  imports: [MatDialogModule],
  templateUrl: './userteam-dialog.component.html',
  styleUrl: './userteam-dialog.component.scss'
})
export class UserteamDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<UserteamDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userTeamService: UserTeamService
  ) {}

  public confirmDelete(): void {
    const teamId = this.data.team.id;
    const userId = this.data.user.id;

    this.userTeamService.removeUserFromTeam(teamId, userId).subscribe(
      (response) => {
        this.dialogRef.close(true); 
      },
      (error) => {
        console.error('Error deleting user:', error);
        this.dialogRef.close(false);  
      }
    );
  }

  public cancel(): void {
    this.dialogRef.close(false);  
  }
}

