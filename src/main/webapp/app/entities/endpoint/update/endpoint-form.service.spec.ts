import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../endpoint.test-samples';

import { EndpointFormService } from './endpoint-form.service';

describe('Endpoint Form Service', () => {
  let service: EndpointFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EndpointFormService);
  });

  describe('Service methods', () => {
    describe('createEndpointFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEndpointFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            url: expect.any(Object),
            method: expect.any(Object),
            enabled: expect.any(Object),
            response: expect.any(Object),
            resource: expect.any(Object),
          })
        );
      });

      it('passing IEndpoint should create a new form with FormGroup', () => {
        const formGroup = service.createEndpointFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            url: expect.any(Object),
            method: expect.any(Object),
            enabled: expect.any(Object),
            response: expect.any(Object),
            resource: expect.any(Object),
          })
        );
      });
    });

    describe('getEndpoint', () => {
      it('should return NewEndpoint for default Endpoint initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEndpointFormGroup(sampleWithNewData);

        const endpoint = service.getEndpoint(formGroup) as any;

        expect(endpoint).toMatchObject(sampleWithNewData);
      });

      it('should return NewEndpoint for empty Endpoint initial value', () => {
        const formGroup = service.createEndpointFormGroup();

        const endpoint = service.getEndpoint(formGroup) as any;

        expect(endpoint).toMatchObject({});
      });

      it('should return IEndpoint', () => {
        const formGroup = service.createEndpointFormGroup(sampleWithRequiredData);

        const endpoint = service.getEndpoint(formGroup) as any;

        expect(endpoint).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEndpoint should not enable id FormControl', () => {
        const formGroup = service.createEndpointFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEndpoint should disable id FormControl', () => {
        const formGroup = service.createEndpointFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
