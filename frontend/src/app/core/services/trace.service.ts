import { Injectable } from '@angular/core';
import { Subject, throwError, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { TraceGroup } from '../models/TraceGroup.model';
import { SortChosen } from '../models/sort.model';
import { HttpErrorResponse, HttpParams } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { ApiService } from './api.service';
import * as moment from 'moment';
import { TimeItem } from '../models/timeItem.model';
import { Trace } from '../models/trace.model';
import { JsonTrace } from '../models/jsonTrace.model';

@Injectable({
  providedIn: 'root'
})
export class TraceService {
  tracesChanged = new Subject<void>();
  sortChanged = new Subject<void>();
  errorOccured = new Subject<void>();
  timeItemChanged = new Subject<void>();

  private traceGroups: TraceGroup[];
  private sortChosen: SortChosen = SortChosen.Longest;
  private error: string;
  private timeItem: TimeItem = {amount: 4, unit: 'h', description: '4 hours'};

  constructor(
    private apiService: ApiService
  ) { }

  getTraceGroups(submittedForm: NgForm): Observable<TraceGroup[]> {
    const params = new HttpParams()
      .set('service', submittedForm.value.uri)
      .set('sort', this.sortChosen)
      .set('from', this.calculateFrom());

    return this.apiService.get('api/trace/uri', params)
                          .pipe(catchError(this.handleError));
  }

  sortTraceGroups(): Observable<TraceGroup[]> {
    const params = new HttpParams()
      .set('sort', this.sortChosen);

    return this.apiService.post('/api/trace/sort', this.traceGroups, params)
                          .pipe(catchError(this.handleError));
  }

  findByCorrelationId(correlationId: string): Observable<JsonTrace> {
    return this.apiService.get(`/api/trace/${correlationId}`)
                          .pipe(catchError(this.handleError));
  }

  getTraces(): TraceGroup[] {
    return this.traceGroups;
  }

  setTraces(traceGroups: TraceGroup[]): void {
    this.traceGroups = traceGroups;
    this.tracesChanged.next();
  }

  getTimeItem(): TimeItem {
    return this.timeItem;
  }

  fetchTraces(tracesGroup: TraceGroup[]): void {
    this.traceGroups = tracesGroup;
    this.tracesChanged.next(); // ked nieco vyhladam a zmeni sa traces vyslem event, kazda komponent ktora subscribuje (pocuva) na zmeny bude vediet ze sa nieco zmenilo
  }

  getSortChosen(): SortChosen {
    return this.sortChosen;
  }

  setSortChosen(sortChosen: SortChosen): void {
    this.sortChosen = sortChosen;
    this.sortChanged.next();
  }

  getError(): string {
    return this.error;
  }

  setError(error: string): void {
    this.error = error;
    this.errorOccured.next();
  }

  clearTraces(): void {
    this.traceGroups = [];
    this.tracesChanged.next();
  }

  quickTimeSelected(timeItem: TimeItem): void {
    this.timeItem = timeItem;
    this.timeItemChanged.next();
  }

  private calculateFrom(): string {
    return moment(new Date()).subtract(this.timeItem.amount, this.timeItem.unit).valueOf() + '';
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      console.error(`Backend returned code ${error.status}, ` + `couldn't get response from server. ElasticSearch is probably down.`);
    }
    return throwError(`<b>Error:</b> couldn't get response from server.<br />Host <b>${environment.servicesBaseUrl}</b> is not responding.`);
  }

}
