import { TestBed, inject } from '@angular/core/testing';

import { SingleAdvertisementService } from './single-advertisement.service';

describe('SingleAdvertisementService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SingleAdvertisementService]
    });
  });

  it('should be created', inject([SingleAdvertisementService], (service: SingleAdvertisementService) => {
    expect(service).toBeTruthy();
  }));
});
