import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagesModalComponent } from './messages-modal.component';

describe('MessagesModalComponent', () => {
  let component: MessagesModalComponent;
  let fixture: ComponentFixture<MessagesModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessagesModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessagesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
