import { IResourceSchema, NewResourceSchema } from './resource-schema.model';

export const sampleWithRequiredData: IResourceSchema = {
  id: '7748d6b6-3a4c-40c9-a992-0f1cd99c932c',
  name: 'Transexual',
  type: 'Modern',
};

export const sampleWithPartialData: IResourceSchema = {
  id: '9f2a1652-35a5-49c7-89f9-dff8639d8c8b',
  name: 'Ohio female',
  type: 'Implemented Qatari orchid',
  fakerMethod: 'Home',
};

export const sampleWithFullData: IResourceSchema = {
  id: '42f43ce6-3714-4e24-b7da-d6839bbf4631',
  name: 'West',
  type: 'farad Berkshire Californium',
  fakerMethod: 'channels Functionality',
};

export const sampleWithNewData: NewResourceSchema = {
  name: 'protocol Northwest Bicycle',
  type: 'Gasoline Corporate hub',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
