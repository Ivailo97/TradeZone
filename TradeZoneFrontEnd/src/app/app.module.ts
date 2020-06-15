import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { AuthInterceptor } from './core/interceptors/auth-interceptor';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoaderService } from './core/services/loader.service';
import { LoaderInterceptor } from './core/interceptors/loader-interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Ng5SliderModule } from 'ng5-slider';
import { AlertModule } from './components/alert';
import { MatTabsModule } from '@angular/material/tabs';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LoaderComponent } from './components/loader/loader.component';
import { CategoryModule } from './components/category/category.module';
import { TopCategoryModule } from './components/top-category/top-category.module';
import { ErrorComponent } from './components/error/error.component';
import { MatMenuModule } from '@angular/material/menu';

@NgModule({

  imports: [
    Ng5SliderModule,
    MatMenuModule,
    BrowserModule,
    AppRoutingModule,
    MatGridListModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AlertModule,
    MatCardModule,
    MatTabsModule,
    CategoryModule,
    TopCategoryModule
  ],
  declarations: [
    AppComponent,
    HomeComponent,
    FooterComponent,
    NavbarComponent,
    LoaderComponent,
    ErrorComponent,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    LoaderService, { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true }
  ],
  exports: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
