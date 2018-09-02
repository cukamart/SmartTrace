import { Component, ViewEncapsulation, Input, OnChanges, SimpleChanges } from '@angular/core';
import { JsonTrace } from '../../core/models/jsonTrace.model';
declare var d3: any;
declare var $: any;

@Component({
  selector: 'app-dependency-graph',
  templateUrl: './dependency-graph.component.html',
  styleUrls: ['./dependency-graph.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DependencyGraphComponent implements OnChanges {
  private readonly ID: string = '#dependency-graph-';

  @Input() idx: string;
  @Input() jsonTrace: JsonTrace; // pass data from item-component if it's defined create graph (check every change)
  graph: any;
  completeIdx: string;
  hidden: boolean;

  constructor() { }

  createDependencyGraph(): void {
    if (this.graph && !this.hidden) {
      $(this.completeIdx).hide();
      this.hidden = true;
      return;
    }

    if (this.hidden) {
      $(this.completeIdx).show();
      this.hidden = false;
      return;
    }

    this.hidden = false;
    this.completeIdx = this.ID + this.idx;

    this.graph = [];
    this.graph.push(JSON.parse(this.jsonTrace.dependencyGraph));

    const margin = { top: 0, right: 0, bottom: 0, left: 200 },
      width = 1400 - margin.right - margin.left,
      height = 550 - margin.top - margin.bottom;

    const zoom = d3.behavior
      .zoom()
      .scaleExtent([-10, 10])
      .on('zoom', zoomed);

    function zoomed() {
      svg.attr(
        'transform',
        'translate(' + d3.event.translate + ')scale(' + d3.event.scale + ')'
      );
    }

    let i = 0;
    const duration = 750;
    let root;

    const tree = d3.layout.cluster().size([height, width]);

    const diagonal = d3.svg.diagonal().projection(function(d) {
      return [d.y, d.x];
    });

    const svg = d3
      .select(this.completeIdx)
      .append('svg:svg')
      .attr('width', width + margin.right + margin.left)
      .attr('height', height + margin.top + margin.bottom)
      .call(zoom)
      .append('g')
      .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');


     root = this.graph[0];
     root.x0 = height / 2;
     root.y0 = 0;

    update(root);

    d3.select(self.frameElement).style('height', '500px');

    function update(source) {
      // Compute the new tree layout.
      const nodes = tree.nodes(root).reverse(),
        links = tree.links(nodes);

      // Normalize for fixed-depth.
      nodes.forEach(function(d) {
        d.y = d.depth * 240;
      });

      // Update the nodes…
      const node = svg.selectAll('g.node').data(nodes, function(d) {
        return d.id || (d.id = ++i);
      });

      // Enter any new nodes at the parent's previous position.
      const nodeEnter = node
        .enter()
        .append('g')
        .attr('class', 'node')
        .attr('transform', function(d) {
          return 'translate(' + source.y0 + ',' + source.x0 + ')';
        })
        .on('click', click);

      nodeEnter
        .append('circle')
        .attr('r', 1e-6)
        .style('fill', function(d) {
          return d._children ? 'lightsteelblue' : '#fff';
        });

      nodeEnter
        .append('text')
        .attr('x', function(d) {
          return d.children || d._children ? -13 : 13;
        })
        .attr('dy', '.35em')
        .attr('text-anchor', function(d) {
          return d.children || d._children ? 'end' : 'start';
        })
        .text(function(d) {
          return d.name;
        })
        .style('fill-opacity', 1e-6);

      // Transition nodes to their new position.
      const nodeUpdate = node
        .transition()
        .duration(duration)
        .attr('transform', function(d) {
          return 'translate(' + d.y + ',' + d.x + ')';
        });

      nodeUpdate
        .select('circle')
        .attr('r', 8)
        .style('fill', function(d) {
          return d._children ? 'lightsteelblue' : '#fff';
        });

      nodeUpdate.select('text').style('fill-opacity', 1);



      // Transition exiting nodes to the parent's new position.
      const nodeExit = node
        .exit()
        .transition()
        .duration(duration)
        .attr('transform', function(d) {
          return 'translate(' + source.y + ',' + source.x + ')';
        })
        .remove();

      nodeExit.select('circle').attr('r', 1e-6);

      nodeExit.select('text').style('fill-opacity', 1e-6);

      // Update the links…
      const link = svg.selectAll('path.link').data(links, function(d) {
        return d.target.id;
      });

      // Enter any new links at the parent's previous position.
      link
        .enter()
        .insert('path', 'g')
        .attr('class', 'link')
        .attr('d', function(d) {
          const o = { x: source.x0, y: source.y0 };
          return diagonal({ source: o, target: o });
        });

      // Transition links to their new position.
      link
        .transition()
        .duration(duration)
        .attr('d', diagonal);

      // Transition exiting nodes to the parent's new position.
      link
        .exit()
        .transition()
        .duration(duration)
        .attr('d', function(d) {
          const o = { x: source.x, y: source.y };
          return diagonal({ source: o, target: o });
        })
        .remove();

      // Stash the old positions for transition.
      nodes.forEach(function(d) {
        d.x0 = d.x;
        d.y0 = d.y;
      });
    }

    // Toggle children on click.
    function click(d) {
      if (d.children) {
        d._children = d.children;
        d.children = null;
      } else {
        d.children = d._children;
        d._children = null;
      }
      update(d);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.jsonTrace.currentValue) {
      this.createDependencyGraph();
    }
  }
}
