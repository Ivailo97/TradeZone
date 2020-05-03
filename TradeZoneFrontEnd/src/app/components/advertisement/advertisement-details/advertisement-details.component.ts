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
    this.photoToDeleteId = invalidPhotoId;
    this.defaultImageUrl = noImage;
    this.isOwnedByUser = this.tokenStorageService.getUsername() === this.advertisement.creator;
    this.hasNoImages = this.advertisement.images.length === 0;
    this.advertisementService.refreshNeeded$.subscribe(() => this.reloadAdvertisement(this.advertisement.id))
    this.advertisementService.updateViews(this.route.snapshot.params.id, this.advertisement.views + 1);
  }

  reloadAdvertisement(id: number) {
    this.advertisementService.getAdvertisement(id)
      .subscribe(
        data => {
          this.advertisement = data;
          this.hasNoImages = this.advertisement.images.length === 0;
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
