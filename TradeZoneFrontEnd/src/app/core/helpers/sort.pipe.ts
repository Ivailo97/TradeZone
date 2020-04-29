import { Pipe, PipeTransform } from '@angular/core';
import { AdvertisementInfoList } from '../models/advertisement-info-list';

@Pipe({
  name: 'sort'
})
export class SortByLikesPipe implements PipeTransform {

  transform(advertisements:AdvertisementInfoList[]): any {
    return advertisements.sort((a,b) => b.profilesWhichLikedIt.length - a.profilesWhichLikedIt.length )
  }

}