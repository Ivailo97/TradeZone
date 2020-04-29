import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Options, LabelType } from 'ng5-slider';

const initialMinValue = 0;
const initialMaxValue = 10000;

@Component({
  selector: 'app-price-range',
  templateUrl: './price-range.component.html',
  styleUrls: ['./price-range.component.css']
})
export class PriceRangeComponent implements OnInit {

  minValue: number;
  maxValue: number;

  @Output() valuesEmmiter = new EventEmitter<number[]>();

  options: Options = {

    floor: initialMinValue,
    ceil: initialMaxValue,
    translate: (value: number, label: LabelType): string => {
      switch (label) {
        case LabelType.Low:
          return '<b>Min price:</b> $' + value;
        case LabelType.High:
          return '<b>Max price:</b> $' + value;
        default:
          return '$' + value;
      }
    }

  };

  ngOnInit(): void {
    this.minValue = initialMinValue;
    this.maxValue = initialMaxValue;
    this.sendValuesToParent();
  }

  sendValuesToParent() {
    this.valuesEmmiter.emit([this.minValue, this.maxValue]);
  }
}
