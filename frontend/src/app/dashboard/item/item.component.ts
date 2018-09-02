import { Component, Input, ViewChild  } from '@angular/core';
import { TraceGroup } from '../../core/models/TraceGroup.model';
import { TraceService } from '../../core/services/trace.service';
import { CytoscapeComponent } from 'ngx-cytoscape';
import { JsonTrace } from '../../core/models/jsonTrace.model';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css']
})
export class ItemComponent  { // todo vysekat cytospace do osobitnej komponenty nech to tu nerobi bordel...
  @ViewChild('cytograph') cytograph: CytoscapeComponent;
  @Input() traceGroup: TraceGroup;
  @Input() last: boolean;
  @Input() idx: string;
  jsonTrace: JsonTrace;
  completeIdx: string;
  private _graphData: any;

  public style: any = [
    {
      selector: 'node',
      style: {
        'background-color': 'data(faveColor)',
        'shape': 'roundrectangle',
        'label': 'data(name)',
        'font-size': '20',
      }
    },
    {
      selector: 'edge',
      style: {
        'width': 3,
        'curve-style': 'bezier',
        'line-color': 'data(faveColor)',
        'target-arrow-color': '#rrr',
        'target-arrow-shape': 'triangle',
        'label': 'data(label)',
        'font-size': '25'
      }
    }
  ];

  constructor(
    private traceService: TraceService
  ) { }

  showDetails(correlationId: string): void {
    this.traceService.findByCorrelationId(correlationId)
    .subscribe(
      (jsonTrace) => {
        this.jsonTrace = jsonTrace;
      },
      (error) => {
        this.traceService.setError(error);
        this.traceService.clearTraces();
      },
      () => {
        this.traceService.setError('');
        this.createGraphData();
      }
    );
  }

  createGraphData(): void {
    if (this._graphData) {
      this._graphData = null;
      return;
    }

    const nodes = this.jsonTrace.nodes;
    const edges = this.jsonTrace.edges;
    this._graphData = { nodes, edges };
  }

  get graphData(): any {
    return this._graphData;
  }

  set graphData(value: any) {
    this._graphData = value;
  }
}
