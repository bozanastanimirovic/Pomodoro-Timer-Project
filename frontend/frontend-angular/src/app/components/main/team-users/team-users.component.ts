import { Component, Input, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';
import { User } from '../../../models/user';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import {MatTableModule} from '@angular/material/table';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { Team } from '../../../models/team';
import { RouterModule } from '@angular/router';
import { UserTeamService } from '../../../services/user-team.service';
import { UserteamDialogComponent } from '../../dialogs/userteam-dialog/userteam-dialog.component';
import { AddUserComponent } from '../../dialogs/add-user/add-user.component';

@Component({
  selector: 'app-team-users',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatToolbarModule,
    MatSnackBarModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatIconModule,
    MatPaginator,
    MatSort,
    RouterModule
  ],
  templateUrl: './team-users.component.html',
  styleUrl: './team-users.component.scss'
})
export class TeamUsersComponent implements OnChanges{
  dataSource!: MatTableDataSource<User>;
  displayedColumns = [
    'id',
    'username',
    'email',
    'name',
    'surname',
    'actions'
  ];
  subscription!: Subscription;

  @Input() childSelectedTeam!: Team;

  @Input() usersInSelectedTeam: User[] = [];

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;


  constructor(
    private userTeamService: UserTeamService,
    private dialog: MatDialog
  ) { }


  ngOnChanges(changes: SimpleChanges): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.loadData();
  }

  public loadData() {
    (this.subscription = this.userTeamService
      .getUsersByTeam(this.childSelectedTeam.id)
      .subscribe(data => {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      })),
      (error: Error) => {
        console.log(error.name + ' ' + error.message);
      };
  }

  public openDeleteDialog(user: User): void {
    const dialogRef = this.dialog.open(UserteamDialogComponent, {
      data: { user: user, team: this.childSelectedTeam },
    });
  
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadData(); 
      }
    });
  }

  public openAddUserDialog(): void {
    const dialogRef = this.dialog.open(AddUserComponent, {
      data: {
        team: this.childSelectedTeam,
        usersInTeam: this.usersInSelectedTeam
      }
    });
  
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadData();  
      }
    });
  }

  public applyFilter(filter:any){
    filter = filter.target.value;
    filter = filter.trim();
    filter = filter.toLocaleLowerCase();
    this.dataSource.filter = filter;
  }

}
