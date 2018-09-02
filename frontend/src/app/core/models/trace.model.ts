export interface Trace {
  id: string;
  correlationId: string;
  protocol: InvocationProtocol;
  direction: InvocationDirection;
  user: string;
  startDate: Date;
  duration: number;
  uri: string;
  method: string;
  namespace: string;
  operation: string;
  statusCode: number;
  topic: string;
  request: Object;
  response: Object;
  msguid: string;
  cidla: string;
  taskId: string;
  nodeId: string;
  externalSystem: string;
}

export enum InvocationProtocol {
  REST, SOAP, KAFKA
}

export enum InvocationDirection {
  INBOUND, OUTBOUND
}
