export interface TraceGroup {
  correlationId: string;
  startDate: Date;
  span: number;
  totalDuration: number;
  uri: string;
}
