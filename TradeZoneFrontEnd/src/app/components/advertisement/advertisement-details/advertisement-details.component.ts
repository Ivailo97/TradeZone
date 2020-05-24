import { Component, OnInit } from '@angular/core';
import { AdvertisementDetails } from 'src/app/core/models/advertisement-details';
import { ActivatedRoute, Router } from '@angular/router';
import { AdvertisementService } from 'src/app/core/services/advertisement.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { AdvertisementInfoList } from 'src/app/core/models/advertisement-info-list';

const noImage = 'https://res.cloudinary.com/knight-cloud/image/upload/v1586614999/xtxuiw3kqqy5wxq6ufla.png'
const invalidPhotoId = -1;

@Component({
  selector: 'app-adretisiment-details',
  templateUrl: './advertisement-details.component.html',
  styleUrls: ['./advertisement-details.component.css']
})
export class AdvertisementDetailsComponent implements OnInit {

  page: number = 0;

  categoryImageUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1589234260/zb0rv6r6ecrafkvia7ph.png';
  conditionImageUlr = 'https://res.cloudinary.com/knight-cloud/image/upload/v1589234279/efhzczebhzn9jdkijokm.png';
  deliveryImageUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1589234299/fscm4lxngph4hqo7b24s.png';

  images = [];

  isOwnedByUser: boolean;

  defaultImageUrl: string;

  advertisement: AdvertisementDetails;

  advertisementsCreatedByUser: AdvertisementInfoList[] = [];

  photoToDeleteId: number;

  hasNoImages: boolean;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private advertisementService: AdvertisementService,
    private tokenStorageService: TokenStorageService) { }

  ngOnInit() {
    this.reloadData(this.route.snapshot.params.id);
    this.advertisementService.detailsRefreshNeeded$.subscribe(() => this.reloadData(this.advertisement.id))
  }

  reloadData(id: number) {
    this.page = 0;
    this.advertisementsCreatedByUser = [];
    this.advertisementService.getAdvertisement(id)
      .subscribe(
        data => {
          this.advertisement = data;
          this.initMoreCreatedByUser();
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

  initMoreCreatedByUser() {
    this.advertisementService.getCreatedByUser(this.advertisement.creator.userUsername, this.page, this.advertisement.id)
      .subscribe(
        advertisements => {
          this.advertisementsCreatedByUser = this.advertisementsCreatedByUser.concat(advertisements);
        }
      )
  }

  goToBottom(){
    window.scrollTo(0,document.body.scrollHeight);
  }

  loadMore() {
    this.page++;
    this.initMoreCreatedByUser();
  }

  changePhotoToDeleteId(id: number) {
    this.photoToDeleteId = id;
  }
}
