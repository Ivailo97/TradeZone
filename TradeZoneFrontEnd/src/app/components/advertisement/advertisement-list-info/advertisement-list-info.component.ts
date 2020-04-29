import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { AdvertisementInfoList } from '../../../core/models/advertisement-info-list';
import { AlertService } from '../../alert';
import { ProfileService } from 'src/app/core/services/profile.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';

const defaultImageURL = 'https://res.cloudinary.com/knight-cloud/image/upload/v1586614999/xtxuiw3kqqy5wxq6ufla.png';

@Component({
  selector: 'app-advertisement-info',
  templateUrl: './advertisement-list-info.component.html',
  styleUrls: ['./advertisement-list-info.component.css']
})
export class AdvertisementListInfoComponent implements OnInit {

  @Input() advertisement: AdvertisementInfoList;

  @Output() emitId = new EventEmitter<number>();

  favoriteIconInfoMessage: string;

  isFavorite: boolean;

  isYours: boolean;

  constructor(private alertService: AlertService,
    private profileService: ProfileService,
    private tokenStorageService: TokenStorageService) {
  }

  ngOnInit() {
    this.advertisement.imageUrl = this.advertisement.imageUrl ? this.advertisement.imageUrl : defaultImageURL;
    this.isYours = this.advertisement.creator === this.tokenStorageService.getUsername();
    this.isFavorite = this.advertisement.profilesWhichLikedIt.includes(this.tokenStorageService.getUsername());
    this.favoriteIconInfoMessage = this.isFavorite ? 'Remove from favorites' : 'Add to favorites';
  }

  flip(event) {
    if (!event.target.classList.contains('far') && !event.target.classList.contains('tools')) {
      const card = event.currentTarget.children[0];
      if (card.classList.contains('active')) {
        card.classList.remove('active')
      } else {
        card.classList.add('active');
      }
    }
  }

  addToFavorite(event) {
    const icon = event.currentTarget;
    if (icon.classList.contains('fav-active')) {

      this.profileService.removeFavoriteAdvertisement(this.advertisement.id).subscribe(
        success => {
          icon.classList.remove('fav-active')
          this.alertService.success('Successfully removed from favorites!', { autoClose: true })
          this.favoriteIconInfoMessage = 'Add to favorites';
        },
        error => {
          this.alertService.error('Already removed!');
        }
      );

    } else {
      this.profileService.addFavoriteAdvertisement(this.advertisement.id).subscribe(
        success => {
          icon.classList.add('fav-active');
          this.alertService.success('Successfully added to favorites!', { autoClose: true })
          this.favoriteIconInfoMessage = 'Remove from favorites';
        },
        error => {
          this.alertService.error('Already added!', { autoClose: true });
        }
      );
    }
  }

  passIdToParent() {
    this.emitId.emit(this.advertisement.id);
  }
}
