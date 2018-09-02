import { Component, OnInit, ViewEncapsulation } from '@angular/core';

declare var Treant: any;
declare var cytoscape: any;

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'app-treant-tree',
  templateUrl: './treant-tree.component.html',
  styleUrls: ['./treant-tree.component.css']
})
export class TreantTreeComponent implements OnInit {
  simple_chart_config = {
    chart: {
        container: '#tree-simple'
    },

    nodeStructure: {
        text: { name: 'Parent node' },
        children: [
            {
                text: { name: 'First child' }
            },
            {
                text: { name: 'Second child' }
            }
        ]
    }
  };

  constructor() { }

  ngOnInit(): void {
    (() => {
      Treant(this.simple_chart_config);
    })();
  }
}
