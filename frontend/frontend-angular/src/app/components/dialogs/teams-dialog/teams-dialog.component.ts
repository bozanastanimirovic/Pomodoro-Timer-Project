import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Team } from '../../../models/team';
import { TeamService } from '../../../services/team.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-teams-dialog',
  standalone: true,
  imports: [MatFormFieldModule, MatSelectModule, CommonModule, FormsModule, MatInputModule, MatButtonModule],
  templateUrl: './teams-dialog.component.html',
  styleUrl: './teams-dialog.component.scss'
})
export class TeamsDialogComponent {

  
  flag!:number;

  constructor(
    public snackBar:MatSnackBar,
    public dialogRef:MatDialogRef<Team>,
    @Inject (MAT_DIALOG_DATA) public data: Team,
    public service:TeamService,
  ){}

  ngOnInit(): void {

  }

  public add(){
    this.service.createTeam(this.data.teamName).subscribe(
      (data) => {
        this.snackBar.open(`Successfully added team: ${data.teamName}`, `OK`, {duration:2500})
        this.dialogRef.close(true);
      }
    ),
    (error:Error) => {
      console.log(error.name + ' ' + error.message);
      this.snackBar.open(`Failed`, `Close`, {duration:1500})
      this.dialogRef.close(false);
    };
  }

  public update(){ 
    this.service.updateTeam(this.data.id, this.data.teamName).subscribe(
      (data) => {
        this.snackBar.open(`Successfully updated team: ${data.teamName}`, `OK`, {duration:2500})
        this.dialogRef.close(true);
            }
    ),
    (error:Error) => {
      console.log(error.name + ' ' + error.message);
      this.snackBar.open(`Failed`, `Close`, {duration:1500})
      this.dialogRef.close(false);
    };
  }

  public delete(){
    this.service.deleteTeam(this.data.id).subscribe(
      (data) => {
        this.snackBar.open(`${data}`, `OK`, {duration:2500})
        this.dialogRef.close(true);
      }
    ),
    (error:Error) => {
      console.log(error.name + ' ' + error.message);
      this.snackBar.open(`Failed`, `Close`, {duration:1500})
      this.dialogRef.close(false);
    };
  }

  public cancel(){
    this.dialogRef.close(true);
    this.snackBar.open(`Canceled`, `Close`, {duration:1500});
  }
}
