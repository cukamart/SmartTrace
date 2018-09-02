export interface Edge {
  data: EdgeData;
}

export interface EdgeData {
  source: string;
  target: string;
  label?: number;
  faveColor: string;
}
