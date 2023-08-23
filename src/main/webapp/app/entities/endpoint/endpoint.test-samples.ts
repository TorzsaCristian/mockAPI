import { IEndpoint, NewEndpoint } from './endpoint.model';

export const sampleWithRequiredData: IEndpoint = {
  id: 'f8dca1f3-7930-4f80-b029-784153940945',
  url: 'https://fixed-monsoon.net',
  method: 'firewall Optional Strategist',
};

export const sampleWithPartialData: IEndpoint = {
  id: 'eea56108-d3bb-4a76-b3a7-86b2531aa89e',
  url: 'https://assured-burning.com/',
  method: 'West',
  enabled: false,
  response: 'Integration occlude backing',
};

export const sampleWithFullData: IEndpoint = {
  id: 'd4bf993d-bbc4-43d2-bfdd-0675de1ad90b',
  url: 'https://easy-myth.name/',
  method: 'weber facilitate',
  enabled: true,
  response: 'Product Gorgeous Southwest',
};

export const sampleWithNewData: NewEndpoint = {
  url: 'https://wonderful-cape.biz/',
  method: 'copying',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
