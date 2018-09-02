import * as moment from 'moment';

export interface TimeItem {
  amount: number;
  unit: moment.unitOfTime.DurationConstructor;
  description: string;
}
