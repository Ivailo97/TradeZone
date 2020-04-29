import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TopAdvertisementInfoComponent } from './top-advertisement-info.component';

describe('TopAdvertisementInfoComponent', () => {
  let component: TopAdvertisementInfoComponent;
  let fixture: ComponentFixture<TopAdvertisementInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TopAdvertisementInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopAdvertisementInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
