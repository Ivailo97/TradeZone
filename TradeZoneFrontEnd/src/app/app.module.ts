import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/shared/home/home.component';
import { AuthInterceptor } from './core/interceptors/auth-interceptor';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { LoaderComponent } from './components/shared/loader/loader.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoaderService } from './core/services/loader.service';
import { LoaderInterceptor } from './core/interceptors/loader-interceptor';
import { FooterComponent } from './components/shared/footer/footer.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Ng5SliderModule } from 'ng5-slider';
import { AlertModule } from './components/alert';
import { MatTabsModule } from '@angular/material/tabs';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { TopAdvertisementInfoComponent } from './components/shared/top-advertisement-info/top-advertisement-info.component'
import { SortByLikesPipe } from './core/helpers/sort.pipe';
import { NgxPaginationModule } from 'ngx-pagination';
import { TopCategoryComponent } from './components/shared/top-category/top-category.component';
import {MatButtonModule} from '@angular/material/button';


@NgModule({

  imports: [
    Ng5SliderModule,
    BrowserModule,
    NgxPaginationModule,
    AppRoutingModule,
    MatGridListModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatDividerModule,
    AlertModule,
    MatCardModule,
    MatTabsModule,
    MatButtonModule
    
  ],
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    FooterComponent,
    LoaderComponent,
    SortByLikesPipe,
    TopAdvertisementInfoComponent,
    TopCategoryComponent,
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    LoaderService, { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true }],
  exports: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
