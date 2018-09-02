import { Component, OnInit, Input } from '@angular/core';
import { TimeItem } from '../../../models/timeItem.model';
import { TraceService } from '../../../services/trace.service';

@Component({
  selector: 'app-time-item',
  templateUrl: './time-item.component.html',
  styleUrls: ['./time-item.component.css']
})
export class TimeItemComponent implements OnInit {

  @Input() timeItem: TimeItem;

  constructor(private traceService: TraceService) { }

  ngOnInit() {
  }

  onQuickTimeSelected(timeItem: TimeItem): void {
    this.traceService.quickTimeSelected(timeItem);
  }

}
