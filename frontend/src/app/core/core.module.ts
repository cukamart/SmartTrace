import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './header/header.component';
import { SharedModule } from '../shared/shared.module';
import { SearchFieldComponent } from './search-field/search-field.component';
import { TimePickerComponent } from './search-field/time-picker/time-picker.component';
import { MomentModule } from 'ngx-moment';
import { TimeItemComponent } from './search-field/time-picker/time-item/time-item.component';
import { TreantTreeComponent } from './treant-tree/treant-tree.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    HttpClientModule,
    MomentModule
  ],
  declarations: [HeaderComponent,
    SearchFieldComponent,
    TimePickerComponent,
    TimeItemComponent,
    TreantTreeComponent
  ],
  exports: [
    HeaderComponent,
    SearchFieldComponent
  ]
})
export class CoreModule { }
