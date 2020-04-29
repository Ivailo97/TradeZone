import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AdvertisementDetails } from '../../models/advertisement-details';
import { AdvertisementService } from '../advertisement.service';

@Injectable({
  providedIn: 'root'
})
export class SingleAdvertisementResolver implements Resolve<AdvertisementDetails> {

  constructor(private advertisementService:AdvertisementService ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){

    const id = route.params['id'];
    return this.advertisementService.getAdvertisement(id);
  };
}
