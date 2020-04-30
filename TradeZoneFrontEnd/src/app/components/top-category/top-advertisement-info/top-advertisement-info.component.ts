import { Component, OnInit, Input } from '@angular/core';
import { AdvertisementInfoList } from 'src/app/core/models/advertisement-info-list';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { ProfileService } from 'src/app/core/services/profile.service';
import { AlertService } from '../../alert';

@Component({
  selector: 'app-top-advertisement-info',
  templateUrl: './top-advertisement-info.component.html',
  styleUrls: ['./top-advertisement-info.component.css']
})
export class TopAdvertisementInfoComponent implements OnInit {

  @Input() advertisement: AdvertisementInfoList;

  liked: boolean;

  constructor(private tokenStorageService: TokenStorageService, private alertService: AlertService, private profileService: ProfileService) { }

  ngOnInit(): void {
    this.liked = this.advertisement.profilesWhichLikedIt.includes(this.tokenStorageService.getUsername());
  }

  toogleFavorite() {

    if (this.advertisement.creator === this.tokenStorageService.getUsername()) {
      this.alertService.error('You cant like your own advertisement!', { autoClose: true })
      return;
    };

    //disslike
    if (this.advertisement.profilesWhichLikedIt.includes(this.tokenStorageService.getUsername())) {
      this.profileService.removeFavoriteAdvertisement(this.advertisement.id).subscribe(
        success => {
          this.alertService.success('Successfully removed from favorites!', { autoClose: true })
        },
        error => {
          this.alertService.error('Already removed!');
        }
      );
      //like
    } else {
      this.profileService.addFavoriteAdvertisement(this.advertisement.id).subscribe(
        success => {
          this.alertService.success('Successfully added to favorites!', { autoClose: true })
        },
        error => {
          this.alertService.error('Already added!', { autoClose: true });
        }
      );
    }
    this.liked = !this.liked;
  }
}
