import { Component, OnInit, Input } from '@angular/core';
import { AdvertisementService } from 'src/app/core/services/advertisement.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { AlertService } from '../../alert';

const invalidPhotoId = -1;

@Component({
  selector: 'app-image-delete-modal',
  templateUrl: './image-delete-modal.component.html',
  styleUrls: ['./image-delete-modal.component.css']
})
export class ImageDeleteModalComponent implements OnInit {

  @Input() photoId: number;

  @Input() advertisementId: number;

  constructor(private advertisementService: AdvertisementService,
    private tokenStorage: TokenStorageService,
    private alertService: AlertService) { }

  ngOnInit(): void {
  }

  deletePhoto() {

    if(this.photoId !== invalidPhotoId){
      this.advertisementService.deletePhoto(this.photoId, this.advertisementId, this.tokenStorage.getUsername()).subscribe(
        success => {
          this.alertService.success('Image deleted!', { autoClose: true })
        },
        fail => {
          this.alertService.error('Error: something went wrong!', { autoClose: true });
        }
      );
    }
  }
}
