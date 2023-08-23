import { IResource, NewResource } from './resource.model';

export const sampleWithRequiredData: IResource = {
  id: 'd732c78b-114f-4d2b-a8e8-1b9648a7fc0f',
  name: 'variant',
};

export const sampleWithPartialData: IResource = {
  id: 'b226ee90-9e87-4eca-9451-31caae78de43',
  name: 'gah Trial',
};

export const sampleWithFullData: IResource = {
  id: 'c03f298a-636d-48d2-8f83-c3c8b6d8f26d',
  name: 'Hip',
  generator: 'Security Plastic',
  count: 1764,
};

export const sampleWithNewData: NewResource = {
  name: 'mid',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
