import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from 'rxjs';
import { TraceService } from '../../core/services/trace.service';
import { TraceGroup } from '../../core/models/TraceGroup.model';
import { PagerService } from '../../core/services/pager.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {
  @Input() tracesGroup: TraceGroup[];
  subscription: Subscription;

  pager: any = {};
  pagedItems: any[];

  constructor(
    private traceService: TraceService,
    private pagerService: PagerService
  ) { }

  ngOnInit() {
    this.subscription = this.traceService.tracesChanged.subscribe(
      () => {
        this.tracesGroup = this.traceService.getTraces();
        this.setPage(1, this.tracesGroup);
      }
    );
    this.setPage(1, this.tracesGroup);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  setPage(page: number, items: TraceGroup[]): void {
    this.pager = this.pagerService.getPager(items.length, page);
    this.pagedItems = items.slice(this.pager.startIndex, this.pager.endIndex + 1);
  }
}
