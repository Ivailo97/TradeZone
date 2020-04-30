import { Component } from '@angular/core';
import { LoaderService } from 'src/app/core/services/loader.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent {

  color: string;
  mode: string;
  value: number;
  isLoading: Subject<boolean>;

  constructor(private loaderService: LoaderService) {
    this.color = 'primary';
    this.mode = 'indeterminate';
    this.value = 50;
    this.isLoading = this.loaderService.isLoading;
  }
}
