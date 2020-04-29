import { AdvertisementModule } from './advertisement.module';

describe('AdvertisementModule', () => {
  let advertisementModule: AdvertisementModule;

  beforeEach(() => {
    advertisementModule = new AdvertisementModule();
  });

  it('should create an instance', () => {
    expect(advertisementModule).toBeTruthy();
  });
});
