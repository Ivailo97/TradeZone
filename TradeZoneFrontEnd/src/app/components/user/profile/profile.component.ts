import { Component, OnInit, ViewChild } from '@angular/core';
import { ProfileService } from 'src/app/core/services/profile.service';
import { UserProfile } from 'src/app/core/models/user-profile';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ProfileUpdate } from 'src/app/core/models/profile-update';
import { AlertService } from '../../alert';
import { MatTabGroup } from '@angular/material/tabs';
import { PasswordUpdate } from 'src/app/core/models/password-update';
import { MustMatch } from 'src/app/core/helpers/matchValidator';
import { AdvertisementModalEditComponent } from '../../advertisement/advertisement-modal-edit/advertisement-modal-edit.component';

const defaultUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1586855234/opzt01swy5uezhzexcql.png';
const emptyCollectionUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1587845075/sqj0rmatvhts7hkmhky1.png';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  selectedFile: File = null;

  advertisementToModifyId: number;

  @ViewChild(AdvertisementModalEditComponent) editComponent: AdvertisementModalEditComponent;

  @ViewChild("tabs") tabs: MatTabGroup;

  defaultProfilePhotoUrl: string;

  emptyCollectionPhotoUrl:string;

  updateProfileForm: FormGroup;

  topRole:string;

  changePasswordForm: FormGroup;

  userProfile: UserProfile;

  constructor(private profileService: ProfileService,
    private formBuilder: FormBuilder,
    private alertService: AlertService) { }

  ngOnInit(): void {

    this.updateProfileForm = this.formBuilder.group({
      id: ['', [Validators.required]],
      firstName: ['', [Validators.required, Validators.pattern(/^[A-ZА-Я]+[a-zа-я]{3,11}$/)]],
      lastName: ['', [Validators.required, Validators.pattern(/^[A-ZА-Я]+[a-zа-я]{3,11}$/)]],
      city: ['', [Validators.required, Validators.pattern(/^[A-ZА-Я]+[a-zа-я]{2,}$/)]],
      country: ['', [Validators.required, Validators.pattern(/^[A-ZА-Я]+[a-zа-я]{2,}$/)]],
      aboutMe: ['', [Validators.required, Validators.pattern(/^[A-ZА-Я]+[a-zа-я ]{1,99}$/)]],
    })

    this.changePasswordForm = this.formBuilder.group({
      id: ['', [Validators.required]],
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.pattern(/^[^\s]{3,16}$/)]],
      newPasswordConfirm: ['', [Validators.required]],
    }, { validator: MustMatch('newPassword', 'newPasswordConfirm') })

    this.defaultProfilePhotoUrl = defaultUrl;
    this.emptyCollectionPhotoUrl = emptyCollectionUrl;
    this.advertisementToModifyId = null;
    this.profileService.refreshNeeded$.subscribe(() => this.loadProfile())
    this.loadProfile();
    
  }

  private loadProfile() {
    this.profileService.getUserProfile().subscribe(data => {
      this.userProfile = data;
      this.topRole = this.initTopRole();
      this.updateProfileForm.reset();
      this.f.id.setValue(this.userProfile.id);
      this.p.id.setValue(this.userProfile.id);
    });
  }

 private initTopRole(){
    
    let roles = this.userProfile.roles;

    let adminRole = roles.find(x => x === 'ROLE_ADMIN');
    let moderatorRole = roles.find(x => x === 'ROLE_MODERATOR');
    let userRole = roles.find(x => x === 'ROLE_USER');

    return adminRole || moderatorRole || userRole;
  }

  update() {

    const updateModel = new ProfileUpdate(
      this.f.id.value,
      this.f.firstName.value,
      this.f.lastName.value,
      this.f.city.value,
      this.f.country.value,
      this.f.aboutMe.value,
    )

    this.profileService
      .update(updateModel)
      .subscribe(
        (success) => {
          this.alertService.success('Updated profile successfully!', { autoClose: true })
          const tabCount = this.tabs._tabs.length;
          this.tabs.selectedIndex = (this.tabs.selectedIndex + 2) % tabCount;
        },
        (fail) => {
          this.alertService.error(`Error: ${fail.error.message}`, { autoClose: true })
        })
  }

  changePassword() {

    const passwordUpdate = new PasswordUpdate(
      this.p.id.value,
      this.p.oldPassword.value,
      this.p.newPassword.value,
      this.p.newPasswordConfirm.value);

    this.profileService
      .updatePassword(passwordUpdate)
      .subscribe(
        (success) => {
          this.alertService.success('Password updated successfully!', { autoClose: true })
          const tabCount = this.tabs._tabs.length;
          this.tabs.selectedIndex = (this.tabs.selectedIndex + 1) % tabCount;
        },
        (fail) => {
          console.log(fail);
          this.alertService.error(fail.error.message, { autoClose: true })
        })
  }

  onTabChanged() {
    this.updateProfileForm.reset();
    this.changePasswordForm.reset();
    this.f.id.setValue(this.userProfile.id);
    this.p.id.setValue(this.userProfile.id);
  }

  changeAdvertisementToModifyId(event) {
    this.advertisementToModifyId = event;
    this.editComponent.refreshAdvertisement();
  }

  onFileSelected(event) {
    this.selectedFile = <File>event.target.files[0];

    const fd = new FormData();
    fd.append('image', this.selectedFile, this.selectedFile.name)

    this.profileService
      .uploadPhoto(fd)
      .subscribe(
        (success) => {
          this.alertService.success(success['message'], { autoClose: true })
        },
        (fail) => {
          this.alertService.error(fail.error.message, { autoClose: true })
        })
  }

  get f() {
    return this.updateProfileForm.controls;
  }

  get p() {
    return this.changePasswordForm.controls;
  }
}
