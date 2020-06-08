import { UserService } from 'src/app/core/services/user.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';
import { AlertService } from '../../alert';
import { ProfileService } from 'src/app/core/services/profile.service';

const defaultProfilePicUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1586855234/opzt01swy5uezhzexcql.png';
const tableColumns = ['id', 'photoUrl', 'userUsername', 'role'];
const alertConfig = { autoClose: true };
const roleChangeSuccess = "Successfully changed role";
const roleChangeError = "Something went wrong";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  displayedColumns: string[];;
  defaultUrl: string;
  dataSource: MatTableDataSource<UserData>;
  roles$: Observable<string[]>

  currentProfileId: number;
  currentTopRole: string;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(private userService: UserService,
    private alertService: AlertService,
    private profileService: ProfileService) {
    this.defaultUrl = defaultProfilePicUrl;
    this.displayedColumns = tableColumns;
  }

  ngOnInit() {
    this.userService.refreshNeeded$.subscribe(() => this.initBoard());
    this.profileService.getUserProfile().subscribe(data => this.currentProfileId = data.id);
    this.roles$ = this.userService.getAllRoles();
    this.initBoard();
  }

  private initBoard() {
    this.userService.getAdminBoard().subscribe(
      data => {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.currentTopRole = data.find(profile => profile.id === this.currentProfileId).role;
      },
      error => {
        console.log(`${error.status}: ${JSON.parse(error.error).message}`)
      }
    );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  public changeRole(newRole: string, profileId: number): void {

    const roleChange = new RoleChange(newRole, profileId);

    this.userService.changeRole(roleChange).subscribe(
      data => {
        this.alertService.success(roleChangeSuccess, alertConfig)
      },
      error => {
        this.alertService.error(roleChangeError, alertConfig)
      }
    )
  }

  public isCurrentUser(id: number): boolean {
    return this.currentProfileId == id;
  }

  public isCurrentRole(role: string, currentRole: string): boolean {
    return role.replace('ROLE_', '').toLocaleLowerCase() === currentRole.toLocaleLowerCase();
  }

  private isRoot(role: string): boolean {
    return role === 'ROOT';
  }

  public canChange(role: string): boolean {
    return !this.isRoot(role) && (this.isRoot(this.currentTopRole) || role !== 'Admin');
  }
}

export class RoleChange {
  newRole: string;
  profileId: number;

  constructor(newRole: string, profileId: number) {
    this.newRole = newRole;
    this.profileId = profileId;
  }
}

export interface UserData {
  id: number;
  photoUrl: string;
  userUsername: string;
  role: string;
}