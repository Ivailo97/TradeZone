import { Component, OnInit, Input, OnChanges, SimpleChanges, SimpleChange, ViewChild } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { AdvertisementService } from 'src/app/core/services/advertisement.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { Observable } from 'rxjs';
import { CategorySelectModel } from 'src/app/core/models/category-select';
import { AlertService } from '../../alert';
import { AdvertisementToEditModel } from 'src/app/core/models/advertisement-to-edit';
import { AdvertisementEditedModel } from 'src/app/core/models/advertisement-edit';

@Component({
  selector: 'app-advertisement-modal-edit',
  templateUrl: './advertisement-modal-edit.component.html',
  styleUrls: ['./advertisement-modal-edit.component.css']
})
export class AdvertisementModalEditComponent implements OnInit, OnChanges {

  form: FormGroup;
  @Input() id: number;

  advertisementToEdit$: Observable<AdvertisementToEditModel>;
  advertisement: AdvertisementEditedModel;
  conditions$: Observable<String[]>;
  categories$: Observable<CategorySelectModel[]>

  constructor(
    private formBuilder: FormBuilder,
    private advertisementService: AdvertisementService,
    private tokenService: TokenStorageService,
    private alertService: AlertService) {
  }
  ngOnChanges(changes: SimpleChanges): void {
    this.refreshAdvertisement();
  }

  public refreshAdvertisement() {

    if (this.id !== null) {
      this.advertisementToEdit$ = this.advertisementService.getAdvertisementToEdit(this.id);
    }
  }

  ngOnInit() {
    this.conditions$ = this.advertisementService.getAllConditions();
    this.categories$ = this.advertisementService.getAllCategories();

    this.form = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0.1)]],
      category: ['', Validators.required],
      condition: ['', Validators.required]
    })
  }

  submit() {

    this.advertisement = new AdvertisementEditedModel(
      this.id,
      this.f.title.value,
      this.f.description.value,
      this.f.price.value,
      this.f.condition.value,
      this.f.category.value,
      this.tokenService.getUsername()
    )

    this.advertisementService.edit(this.advertisement)
      .subscribe(
        success => {
          this.alertService.success('Edited successfully!', { autoClose: true });
          this.form.reset();
        },
        fail => {
          this.alertService.error(fail.error.message, { autoClose: true });
          this.form.reset();
        })
  }

  get f() {
    return this.form.controls;
  }
}
