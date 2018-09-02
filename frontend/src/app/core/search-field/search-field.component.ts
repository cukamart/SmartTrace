import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TraceService } from '../services/trace.service';

@Component({
  selector: 'app-search-field',
  templateUrl: './search-field.component.html',
  styleUrls: ['./search-field.component.css']
})
export class SearchFieldComponent implements OnInit {

  constructor(private traceService: TraceService) { }

  onSubmit(submittedForm: NgForm): void {
    if (submittedForm.invalid) {
      return;
    }
    this.traceService.getTraceGroups(submittedForm)
    .subscribe(
      (traceGroups) => {
        this.traceService.fetchTraces(traceGroups);
      },
      (error) => {
        this.traceService.setError(error);
        this.traceService.clearTraces();
      },
      () => {
        this.traceService.setError(''); // If I don't want handle error msg every time I would need to show error in popup or navigate user to different page
      }
    );
  }

  ngOnInit(): void {

  }
}
