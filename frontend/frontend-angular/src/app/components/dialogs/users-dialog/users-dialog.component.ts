import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button';
import { User } from '../../../models/user';
import { UserService } from '../../../services/user.service';
import { Role } from '../../../models/role';
import { UserRole } from '../../../models/enums/userRole';

@Component({
  selector: 'app-users-dialog',
  standalone: true,
  imports: [MatFormFieldModule, MatSelectModule, CommonModule, FormsModule, MatInputModule, MatButtonModule],
  templateUrl: './users-dialog.component.html',
  styleUrl: './users-dialog.component.scss'
})
export class UsersDialogComponent {
  flag!:number;
  roles: Role[] = [
    { id: 1, roleName: UserRole.RoleAdmin },
    { id: 2, roleName: UserRole.RoleUser }
  ];

  constructor(
    public snackBar:MatSnackBar,
    public dialogRef:MatDialogRef<User>,
    @Inject (MAT_DIALOG_DATA) public data: User,
    public service:UserService,
  ){}

  ngOnInit(): void {

  }

  public compare(a:any, b:any){
    return a.id == b.id;
  }


  public updateRole(): void {
    const newRoleName = this.data.role.roleName; 
    
    this.service.updateUserRole(newRoleName, this.data.id).subscribe(
      () => {
        this.snackBar.open(`Successfully updated user role to: ${newRoleName}`, 'OK', { duration: 2500 });
        this.dialogRef.close(true); 
      },
      (error) => {
        console.error('Failed to update role:', error);
        this.snackBar.open('Failed to update user role.', 'Close', { duration: 2500 });
      }
    );
  }

  deleteUser() {
    this.service.deleteUserManager(this.data.id).subscribe(
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
    this.dialogRef.close();
    this.snackBar.open(`Canceled`, `Close`, {duration:1500});
  }
}
