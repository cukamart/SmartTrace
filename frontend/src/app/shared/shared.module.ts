import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OcticonDirective } from './octicon.directive';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    OcticonDirective
  ],
  exports: [
    OcticonDirective
  ]
})
export class SharedModule { }
