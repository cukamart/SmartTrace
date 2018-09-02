import { Component, OnInit, OnDestroy } from '@angular/core';
import { TraceService } from '../../core/services/trace.service';
import { Subscription } from 'rxjs';
import { TraceGroup } from '../../core/models/TraceGroup.model';
import { SortChosen } from '../../core/models/sort.model';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.css']
})
export class TabsComponent implements OnInit, OnDestroy {

  traceGroups: TraceGroup[];
  sortChosen: SortChosen = SortChosen.Longest;
  error: string;
  SortChosen = SortChosen; // this is a must so I can pass enum from html to typescript component
  subscription: Subscription; // subscription to changes (in traceService)

  constructor(
    private traceService: TraceService
  ) { }

  getTraces(): TraceGroup[] {
    return this.traceGroups;
  }

  // chytam sa len naozaj na to co sa zmeni, ak kliknem sort tak sa chytam len na sortChanged subscription :)
  ngOnInit() {
    this.subscription = this.traceService.tracesChanged.subscribe(
      () => this.traceGroups = this.traceService.getTraces()
    );
    this.subscription = this.traceService.sortChanged.subscribe(
      () => this.sortChosen = this.traceService.getSortChosen()
    );
    this.subscription = this.traceService.errorOccured.subscribe(
      () =>  this.error = this.traceService.getError()
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe(); // custom subscriptions must be cleared.
  }

  onSortChoose(sortChosen: SortChosen): void {
    this.traceService.setSortChosen(sortChosen);
    this.traceService.sortTraceGroups()
      .subscribe(
        traceGroupsSorted => {
          this.traceService.setTraces(traceGroupsSorted);
        },
        (error) => {
          this.error = error;
          this.traceGroups = [];
        },
        () => {
          this.error = '';
        }
      );
  }
}
