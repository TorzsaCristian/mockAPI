import { IMock, NewMock } from './mock.model';

export const sampleWithRequiredData: IMock = {
  id: '36325e4d-83aa-4acd-aec2-a45a7ce7af30',
};

export const sampleWithPartialData: IMock = {
  id: '76bbead3-fc72-4341-add6-098023d37801',
  version: 2162,
};

export const sampleWithFullData: IMock = {
  id: '92c43db1-e23c-44fc-9b83-5df2c70178af',
  prefix: 'Product Avon',
  version: 14215,
};

export const sampleWithNewData: NewMock = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
