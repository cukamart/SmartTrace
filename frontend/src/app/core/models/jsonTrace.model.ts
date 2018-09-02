import { Edge } from './edge.model';

export interface JsonTrace {
  dependencyGraph: string;
  nodes: Node[];
  edges: Edge[];
}
