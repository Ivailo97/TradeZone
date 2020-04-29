import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementListInfoComponent } from './advertisement-list-info.component';

describe('AdvertisementInfoComponent', () => {
  let component: AdvertisementListInfoComponent;
  let fixture: ComponentFixture<AdvertisementListInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvertisementListInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvertisementListInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
