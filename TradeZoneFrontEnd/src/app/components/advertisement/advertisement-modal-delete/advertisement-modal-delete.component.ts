import { Component, OnInit, Input } from '@angular/core';
import { AdvertisementService } from 'src/app/core/services/advertisement.service';
import { AlertService } from '../../alert';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';

@Component({
  selector: 'app-advertisement-modal-delete',
  templateUrl: './advertisement-modal-delete.component.html',
  styleUrls: ['./advertisement-modal-delete.component.css']
})
export class AdvertisementModalDeleteComponent implements OnInit {

  @Input() advertisementId: number;

  constructor(private advertisementService: AdvertisementService,
    private alertService: AlertService,
    private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
  }

  deleteAdvertisement() {

    this.advertisementService.delete(this.advertisementId, this.tokenStorageService.getUsername()).subscribe(
      success => {
        this.alertService.success('Successfully deleted!', { autoClose: true })
      },
      error => {
        this.alertService.error('Already deleted!');
      }
    );;
  }
}
