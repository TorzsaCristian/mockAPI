import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: '0003db68-1df7-4f89-9307-0fba9560590f',
  name: 'Account Jamaican Pollich',
};

export const sampleWithPartialData: IProject = {
  id: '223a785a-c3ce-4a86-9705-16514d1cf08b',
  resourceTreeId: 'Wooden',
  name: 'Consultant bifurcated',
  prefix: 'primary Central mobile',
};

export const sampleWithFullData: IProject = {
  id: '2d38b8c9-d6e1-437a-af43-b48eb84d66eb',
  resourceTreeId: 'Frozen after purple',
  name: 'Bedfordshire',
  prefix: 'orchid Cheese',
  isPublic: true,
  createdAt: dayjs('2023-08-22T11:28'),
};

export const sampleWithNewData: NewProject = {
  name: 'fuchsia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
