import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConversationModalComponent } from './conversation-modal.component';

describe('ConversationModalComponent', () => {
  let component: ConversationModalComponent;
  let fixture: ComponentFixture<ConversationModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConversationModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConversationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
