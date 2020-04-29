import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AdvertisementService } from 'src/app/core/services/advertisement.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { AlertService } from '../../alert';

@Component({
  selector: 'app-image-upload-modal',
  templateUrl: './image-upload-modal.component.html',
  styleUrls: ['./image-upload-modal.component.css']
})
export class ImageUploadModalComponent implements OnInit {

  @Input() advertisementId: number;

  form: FormGroup;

  images: string[] = [];

  constructor(private formBuilder: FormBuilder,
    private advertisementService: AdvertisementService,
    private tokenStorage: TokenStorageService,
    private alertService: AlertService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({ file: ['', Validators.required] })
  }

  uploadPhotos() {
    this.advertisementService.uploadPhotos(this.advertisementId,this.images, this.tokenStorage.getUsername())
    .subscribe(
      success => {
        this.alertService.success('Images uploaded successfully!', { autoClose: true })
      },
      fail => {
        this.alertService.error('Error: something went wrong!', { autoClose: true });
      }
    );
  }

  onFileChange(event) {

    if (event.target.files && event.target.files[0]) {

      var filesAmount = event.target.files.length;

      for (let i = 0; i < filesAmount; i++) {

        var reader = new FileReader();

        reader.onload = (event: any) => {
          this.images.push(event.target.result);
          this.form.patchValue({ images: this.images });
        }

        reader.readAsDataURL(event.target.files[i]);
      }
    }
  }

  resetImages() {
    this.form.reset();
    this.images = [];
  }
}
