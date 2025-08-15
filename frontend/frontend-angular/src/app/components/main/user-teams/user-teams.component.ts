import { Component, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { TeamService } from '../../../services/team.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { Team } from '../../../models/team';
import { Subscription } from 'rxjs';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { AppNavbar } from '../../utility/navbar/navbar.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { User } from '../../../models/user';
import { UserTeamService } from '../../../services/user-team.service';
import { TeamUsersComponent } from "../team-users/team-users.component";

@Component({
  selector: 'app-user-teams',
  standalone: true,
  imports: [
    MatTableModule,
    MatToolbarModule,
    MatSnackBarModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatIconModule,
    AppNavbar,
    MatPaginatorModule,
    MatSort,
    CommonModule,
    TeamUsersComponent
],
  templateUrl: './user-teams.component.html',
  styleUrls: ['./user-teams.component.scss']
})
export class UserTeamsComponent implements OnChanges {
  dataSource!: MatTableDataSource<Team>;
  displayedColumns = ['id', 'teamName']; 
  subscription!: Subscription;
  parentSelectedTeam!:Team;
  usersInSelectedTeam: User[] = []; 

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  constructor(
    private teamService: TeamService, 
    private userTeamService: UserTeamService,) {}

  ngOnInit(): void {
    this.loadData(); 
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe(); 
    }
  }

  loadData(): void {
    this.subscription = this.teamService.getUserTeams().subscribe({
      next: (data: Team[]) => {
        this.dataSource = new MatTableDataSource(data); 
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (error) => {
        console.error('Error loading teams:', error);
      }
    });
  }

  public selectRow(row:Team){
    this.parentSelectedTeam = row;
    console.log("test");
  }

  public loadUsersForSelectedTeam(teamId: number) {
    this.userTeamService.getUsersByTeam(teamId).subscribe(
      (users) => {
        this.usersInSelectedTeam = users; 
      },
      (error) => {
        console.error('Error loading users for team: ', error);
      }
    );
  }
}
