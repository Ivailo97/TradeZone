import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { AdvertisementInfoList } from '../../../core/models/advertisement-info-list';
import { AdvertisementService } from '../../../core/services/advertisement.service';
import { map } from 'rxjs/operators';
import { AdvertisementModalEditComponent } from '../advertisement-modal-edit/advertisement-modal-edit.component';


const initialMinValue = 0;
const initialMaxValue = 10000;
const defaultCategory = 'All';
const defaultCondition = 'All';
const defaultSelectedCriteria = 'none';
const defaultSortCriterias = ['price', 'views', 'title'];
const sortMap = {
  price: (a, b) => a['price'] - b['price'],
  views: (a, b) => a['views'] - b['views'],
  title: (a, b) => a['title'].localeCompare(b['title'])
}

@Component({
  selector: 'app-advertisement-list',
  templateUrl: './advertisement-list.component.html',
  styleUrls: ['./advertisement-list.component.css']
})
export class AdvertisementListComponent implements OnInit {

  @ViewChild(AdvertisementModalEditComponent) editComponent: AdvertisementModalEditComponent;

  config: any;

  descending: boolean;
  selectedCondition: string;
  conditions$: Observable<String[]>
  sortCriterias: string[];
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
    this.conditions$ = this.advertisementService.getAllConditions();
    this.descending = false;
    this.selectedSortCriteria = defaultSelectedCriteria;
    this.selectedCondition = defaultCondition;
    this.sortCriterias = defaultSortCriterias;
    this.currentCategory = defaultCategory;
    this.minPriceRange = initialMinValue;
    this.maxPriceRange = initialMaxValue;
    this.advertisementToModifyId = null;
    this.advertisementService.refreshNeeded$.subscribe(() => this.loadAdvertisements())
    this.loadAdvertisements();
  }

  private loadAdvertisements() {
    this.advertisements$ = this.advertisementService.getAllAdvertisementsWithPriceBetween(this.minPriceRange, this.maxPriceRange);
  }

  changePage(event) {
    this.config.currentPage = event;
  }

  search() {
    if (this.searchText !== '' && this.searchText !== undefined) {

      if (this.currentCategory === defaultCategory) {
        this.advertisements$ = this.advertisementService.getAdvertisementsByTitleAndPriceBetween(this.searchText, this.minPriceRange, this.maxPriceRange);
      } else {
        this.advertisements$ = this.advertisementService.getAdvertisementsByTitleCategoryAndPriceBetween(this.searchText, this.currentCategory, this.minPriceRange, this.maxPriceRange);
      }

    } else {
      this.advertisements$ = this.advertisementService.getAllByCategoryAndPriceBetween(this.currentCategory, this.minPriceRange, this.maxPriceRange);
    }

    this.sort();
  }

  categoryChange(data) {
    if (data !== undefined && data !== '' && data !== null) {
      this.currentCategory = data.trim();
      this.advertisements$ = this.advertisementService.getAllByCategoryAndPriceBetween(data.trim(), this.minPriceRange, this.maxPriceRange);
      this.filterByCondition();
      this.sort();
    }
  }

  sort() {

    if (this.selectedSortCriteria !== defaultSelectedCriteria) {
      this.advertisements$ = this.advertisements$.pipe(
        map(x => x.sort((a, b) => {
          if (this.descending) {
            let temp = a;
            a = b;
            b = temp;
          }
          return sortMap[this.selectedSortCriteria](a, b)
        }))
      )
    }
  }

  filterByCondition() {

    if (this.selectedCondition !== defaultCondition) {

      this.advertisements$ = this.advertisements$.pipe(map(x => x.filter(y => y.condition === this.selectedCondition)))
    } else {
      this.search();
    }
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
}