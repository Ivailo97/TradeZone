import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AdvertisementBindingModel } from 'src/app/core/models/advertisement-create';
import { Observable } from 'rxjs';
import { CategorySelectModel } from 'src/app/core/models/category-select';
import { AdvertisementService } from 'src/app/core/services/advertisement.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';

@Component({
  selector: 'app-advertisement-modal-create',
  templateUrl: './advertisement-modal-create.component.html',
  styleUrls: ['./advertisement-modal-create.component.css']
})
export class AdvertisementModalCreateComponent implements OnInit {

  images = [];
  form: FormGroup;
  advertisement: AdvertisementBindingModel;
  errorMessage;
  conditions$: Observable<String[]>;
  deliveries$: Observable<String[]>;
  categories$: Observable<CategorySelectModel[]>

  constructor(
    private formBuilder: FormBuilder,
    private advertisementService: AdvertisementService,
    private tokenService: TokenStorageService) {
  }

  ngOnInit() {

    this.conditions$ = this.advertisementService.getAllConditions();
    this.deliveries$ = this.advertisementService.getAllDeliveries();
    this.categories$ = this.advertisementService.getAllCategories();

    this.form = this.formBuilder.group({
      title: ['', [Validators.required, Validators.pattern(/^[A-ZА-Я][A-ZА-Яа-яa-z\s\d]{4,19}$/)]],
      description: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0.1), Validators.max(10000)]],
      file: ['', Validators.required],
      category: ['', Validators.required],
      delivery: ['', Validators.required],
      fileSource: ['', Validators.required],
      condition: ['', Validators.required]
    })
  }

  submit() {

    this.advertisement = new AdvertisementBindingModel(
      this.f.title.value,
      this.f.description.value,
      this.f.fileSource.value,
      this.f.price.value,
      this.f.condition.value,
      this.f.category.value,
      this.f.delivery.value,
      this.tokenService.getUsername()
    )

    this.advertisementService.createAdvertisement(this.advertisement)
      .subscribe(
        data => {
          console.log(data);
        },
        error => {
          this.errorMessage = error.error.message;
        })
  }

  get f() {
    return this.form.controls;
  }

  onFileChange(event) {

    if (event.target.files && event.target.files[0]) {

      var filesAmount = event.target.files.length;

      for (let i = 0; i < filesAmount; i++) {

        var reader = new FileReader();

        reader.onload = (event: any) => {

          this.images.push(event.target.result);

          this.form.patchValue({
            fileSource: this.images
          });
        }

        reader.readAsDataURL(event.target.files[i]);
      }
    }
  }

  resetForm() {
    this.images = [];
    this.form.reset();
  }
}
