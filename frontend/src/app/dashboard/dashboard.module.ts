import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TabsComponent } from './tabs/tabs.component';
import { ListComponent } from './list/list.component';
import { ItemComponent } from './item/item.component';

import { CytoscapeModule } from 'ngx-cytoscape';
import { DependencyGraphComponent } from './dependency-graph/dependency-graph.component';

@NgModule({
  imports: [
    CommonModule,
    CytoscapeModule
  ],
  declarations: [
    TabsComponent,
    ListComponent,
    ItemComponent,
    DependencyGraphComponent
  ],
  exports: [
    TabsComponent
  ]
})
export class DashboardModule { }
