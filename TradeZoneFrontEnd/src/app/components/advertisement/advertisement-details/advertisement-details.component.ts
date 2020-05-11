import { Component, OnInit } from '@angular/core';
import { AdvertisementDetails } from 'src/app/core/models/advertisement-details';
import { ActivatedRoute, Router } from '@angular/router';
import { AdvertisementService } from 'src/app/core/services/advertisement.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';

const noImage = 'https://res.cloudinary.com/knight-cloud/image/upload/v1586614999/xtxuiw3kqqy5wxq6ufla.png'
const invalidPhotoId = -1;

@Component({
  selector: 'app-adretisiment-details',
  templateUrl: './advertisement-details.component.html',
  styleUrls: ['./advertisement-details.component.css']
})
export class AdvertisementDetailsComponent implements OnInit {

  categoryImageUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1589234260/zb0rv6r6ecrafkvia7ph.png';
  conditionImageUlr = 'https://res.cloudinary.com/knight-cloud/image/upload/v1589234279/efhzczebhzn9jdkijokm.png';
  deliveryImageUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1589234299/fscm4lxngph4hqo7b24s.png';


  images = [];

  isOwnedByUser: boolean;

  defaultImageUrl: string;

  advertisement: AdvertisementDetails;

  photoToDeleteId: number;

  hasNoImages: boolean;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private advertisementService: AdvertisementService,
    private tokenStorageService: TokenStorageService) { }

  ngOnInit() {
    this.reloadAdvertisement(this.route.snapshot.params.id);
    this.advertisementService.detailsRefreshNeeded$.subscribe(() => this.reloadAdvertisement(this.advertisement.id))
  }

  reloadAdvertisement(id: number) {
    this.advertisementService.getAdvertisement(id)
      .subscribe(
        data => {
          this.advertisement = data;
          this.hasNoImages = this.advertisement.images.length === 0;
          this.photoToDeleteId = invalidPhotoId;
          this.defaultImageUrl = noImage;
          this.isOwnedByUser = this.tokenStorageService.getUsername() === this.advertisement.creator.userUsername;
          this.advertisementService.updateViews(this.route.snapshot.params.id, this.advertisement.views + 1, this.tokenStorageService.getUsername());
        },
        error => {
          this.router.navigate(['/error/', error.error.message])
        },
      );
  }

  changePhotoToDeleteId(id: number) {
    this.photoToDeleteId = id;
  }
}
