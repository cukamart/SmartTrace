import { Component, OnInit, OnDestroy } from '@angular/core';
import { TimeItem } from '../models/timeItem.model';
import { TraceService } from '../services/trace.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  timeItem: TimeItem;
  subscription: Subscription;

  constructor(private traceService: TraceService) { }

  ngOnInit(): void {
    this.timeItem = this.traceService.getTimeItem();
    this.subscription = this.traceService.timeItemChanged.subscribe(
      () => this.timeItem = this.traceService.getTimeItem()
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
