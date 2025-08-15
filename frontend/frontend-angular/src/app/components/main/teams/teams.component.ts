import { Component, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { TeamService } from '../../../services/team.service';
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
import { AppNavbar } from '../../utility/navbar/navbar.component';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { Team } from '../../../models/team';
import { TeamUsersComponent } from "../team-users/team-users.component";
import { UserTeamService } from '../../../services/user-team.service';
import { MatCardModule } from '@angular/material/card'; 
import { MatListModule } from '@angular/material/list'; 
import { RouterModule } from '@angular/router';
import { TeamsDialogComponent } from '../../dialogs/teams-dialog/teams-dialog.component';

@Component({
  selector: 'app-teams',
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
    AppNavbar,
    MatPaginator,
    MatSort,
    TeamUsersComponent,
    MatCardModule,
    MatListModule,
    RouterModule,
],
  templateUrl: './teams.component.html',
  styleUrl: './teams.component.scss'
})
export class TeamsComponent implements OnChanges{

  dataSource!: MatTableDataSource<Team>;
  displayedColumns = [
    'id',
    'teamName',
    'actions'
  ];
  subscription!: Subscription;
  parentSelectedTeam!:Team;
  usersInSelectedTeam: User[] = []; 

  @ViewChild(MatSort, { static: false }) sort!: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;


  constructor(
    private teamService: TeamService,
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
    (this.subscription = this.teamService
      .getAllTeams()
      .subscribe(data => {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      })),
      (error: Error) => {
        console.log(error.name + ' ' + error.message);
      };
  }

  public openDialog(
    flag: number,
    id?: number,
    teamName?: string
  ): void {
    const dialogRef = this.dialog.open(TeamsDialogComponent, {
      data: { id, teamName},
    });
    dialogRef.componentInstance.flag = flag;
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadData(); 
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

  public applyFilter(filter:any){
    filter = filter.target.value;
    filter = filter.trim();
    filter = filter.toLocaleLowerCase();
    this.dataSource.filter = filter;
  }
}
