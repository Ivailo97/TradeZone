import { Component, OnInit } from '@angular/core';
import { AdvertisementDetails } from 'src/app/core/models/advertisement-details';
import { ActivatedRoute } from '@angular/router';
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
    private advertisementService: AdvertisementService,
    private tokenStorageService: TokenStorageService) { }

  ngOnInit() {
    this.photoToDeleteId = invalidPhotoId;
    this.defaultImageUrl = noImage;
    this.advertisement = this.route.snapshot.data['singleAdvertisement'];
    this.isOwnedByUser = this.tokenStorageService.getUsername() === this.advertisement.creator;
    this.hasNoImages = this.advertisement.images.length === 0;
    this.advertisementService.refreshNeeded$.subscribe(() => this.refreshAdvertisement())
    this.advertisementService.updateViews(this.route.snapshot.params.id, this.advertisement.views + 1);
  }

  refreshAdvertisement() {
    this.advertisementService.getAdvertisement(this.advertisement.id).subscribe(data => {
      this.advertisement = data;
      this.hasNoImages = this.advertisement.images.length === 0;
    });
  }

  changePhotoToDeleteId(id: number) {
    this.photoToDeleteId = id;
  }
}
