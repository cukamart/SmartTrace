import { Component, OnInit, Input } from '@angular/core';
import { TimeButton } from '../../models/timeButton.model';
import { TimeItem } from '../../models/timeItem.model';

@Component({
  selector: 'app-time-picker',
  templateUrl: './time-picker.component.html',
  styleUrls: ['./time-picker.component.css']
})
export class TimePickerComponent implements OnInit {

  TimeButton = TimeButton;
  timeButton: TimeButton = TimeButton.Quick;

  @Input() timeItemList1: TimeItem[] = [
    { amount: 15, unit: 'm', description: '15 minutes'},
    { amount: 30, unit: 'm', description: '30 minutes'},
    { amount: 1, unit: 'h', description: '1 hour'},
    { amount: 4, unit: 'h', description: '4 hour'},
    { amount: 12, unit: 'h', description: '12 hour'},
    { amount: 24, unit: 'h', description: '24 hour'},
    { amount: 7, unit: 'd', description: '7 days'}
  ];

  @Input() timeItemList2: TimeItem[] = [
    { amount: 30, unit: 'd', description: '30 days'},
    { amount: 60, unit: 'd', description: '60 days'},
    { amount: 90, unit: 'd', description: '90 days'},
    { amount: 6, unit: 'months', description: '6 months'},
    { amount: 1, unit: 'y', description: '1 year'},
    { amount: 2, unit: 'y', description: '2 years'},
    { amount: 5, unit: 'y', description: '5 years'}
  ];

  constructor() { }

  ngOnInit() {
  }

  onTimeButtonClick(timeButton: TimeButton): void {
    this.timeButton = timeButton;
  }
}
