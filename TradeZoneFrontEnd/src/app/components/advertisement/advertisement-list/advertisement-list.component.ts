import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { AdvertisementInfoList } from '../../../core/models/advertisement-info-list';
import { AdvertisementService } from '../../../core/services/advertisement.service';
import { AdvertisementModalEditComponent } from '../advertisement-modal-edit/advertisement-modal-edit.component';

const initialMinValue = 0;
const initialMaxValue = 10000;
const defaultCategory = 'All';
const defaultCondition = 'All';
const defaultSelectedCriteria = 'none';
const defaultSortCriterias = ['price', 'views', 'title'];

@Component({
  selector: 'app-advertisement-list',
  templateUrl: './advertisement-list.component.html',
  styleUrls: ['./advertisement-list.component.css']
})
export class AdvertisementListComponent implements OnInit {

  @ViewChild(AdvertisementModalEditComponent) editComponent: AdvertisementModalEditComponent;

  config: any;
  descending: boolean;
  sortCriterias: string[];
  selectedCondition: string;
  conditions$: Observable<String[]>
  selectedSortCriteria: string;
  currentCategory: string;
  minPriceRange: number;
  maxPriceRange: number;
  advertisements$: Observable<AdvertisementInfoList[]>;
  startIndex: number = 0;
  endIndex: number = 8;
  currentPage: number = 1;
  searchText: string;
  advertisementToModifyId: number;

  constructor(private advertisementService: AdvertisementService) { }

  ngOnInit() {
    this.config = { id: 'list', itemsPerPage: 6, currentPage: 1 }
    this.advertisementService.getTotalCount().subscribe(
      data => {
        this.config.totalItems = data;
      }
    )
    this.conditions$ = this.advertisementService.getAllConditions();
    this.descending = false;
    this.selectedSortCriteria = defaultSelectedCriteria;
    this.selectedCondition = defaultCondition;
    this.sortCriterias = defaultSortCriterias;
    this.currentCategory = defaultCategory;
    this.minPriceRange = initialMinValue;
    this.maxPriceRange = initialMaxValue;
    this.advertisementToModifyId = null;
    this.advertisementService.refreshNeeded$.subscribe(() => this.loadAdvertisements(this.selectedSortCriteria, "none", this.selectedCondition, this.currentCategory))
    this.loadAdvertisements(this.selectedSortCriteria, "none", this.selectedCondition, this.currentCategory);
  }

  changePage(event) {
    this.config.currentPage = event;
    this.search();
  }

  search() {

    const sortOrder = this.descending ? 'descending' : 'ascending';

    if (this.searchText !== '' && this.searchText !== undefined) {

      this.refreshPaginationCountBeforeSearch();

      this.advertisements$ = this.advertisementService
        .getAdvertisementsByTitleContainingCategoryPriceBetweenAndCondition(this.searchText, this.currentCategory, this.minPriceRange, this.maxPriceRange, this.config.currentPage, this.selectedSortCriteria, sortOrder, this.selectedCondition);

    } else {

      this.refreshPaginationCount();
      this.loadAdvertisements(this.selectedSortCriteria, sortOrder, this.selectedCondition, this.currentCategory);
    }
  }

  categoryChange(data) {
    if (data !== undefined && data !== '' && data !== null) {
      this.currentCategory = data.trim();
      this.config.currentPage = 1;
      this.filterByCondition();
    }
  }

  sort() {
    if (this.selectedSortCriteria !== defaultSelectedCriteria) {
      this.changePage(this.config.currentPage)
    }
  }

  filterByCondition() {
    this.search();
  }

  addOrderTypeInSorting() {
    if (this.selectedSortCriteria !== defaultSelectedCriteria) {
      this.sort();
    }
  }

  priceRangeChange(event) {
    this.minPriceRange = event[0];
    this.maxPriceRange = event[1];
  }

  changeAdvertisementToModifyId(event) {
    this.advertisementToModifyId = event;
    this.editComponent.refreshAdvertisement();
  }

  private refreshPaginationCount() {

    if (this.currentCategory === defaultCategory) {

      this.advertisementService.getCountByPriceBetweenAndCondition(this.minPriceRange, this.maxPriceRange, this.selectedCondition)
        .subscribe(
          data => {
            this.config.totalItems = data;
          }
        )

    } else {

      this.advertisementService.getCountByCategoryPriceBetweenAndCondition(this.currentCategory, this.minPriceRange, this.maxPriceRange, this.selectedCondition)
        .subscribe(
          data => {
            this.config.totalItems = data;
          }
        )
    }
  }

  private refreshPaginationCountBeforeSearch() {


    this.advertisementService.getCountBeforeSearch(this.minPriceRange, this.maxPriceRange, this.selectedCondition, this.currentCategory, this.searchText)
      .subscribe(
        data => {
          this.config.totalItems = data;
        }
      )

  }

  private loadAdvertisements(sortBy, order, condition, category) {
    this.advertisements$ = this.advertisementService.getAllAdvertisementsWithPriceBetween(this.minPriceRange, this.maxPriceRange, this.config.currentPage, sortBy, order, condition, category);
  }
}