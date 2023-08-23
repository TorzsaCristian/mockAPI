import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../mock.test-samples';

import { MockFormService } from './mock-form.service';

describe('Mock Form Service', () => {
  let service: MockFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MockFormService);
  });

  describe('Service methods', () => {
    describe('createMockFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMockFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prefix: expect.any(Object),
            version: expect.any(Object),
          })
        );
      });

      it('passing IMock should create a new form with FormGroup', () => {
        const formGroup = service.createMockFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prefix: expect.any(Object),
            version: expect.any(Object),
          })
        );
      });
    });

    describe('getMock', () => {
      it('should return NewMock for default Mock initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMockFormGroup(sampleWithNewData);

        const mock = service.getMock(formGroup) as any;

        expect(mock).toMatchObject(sampleWithNewData);
      });

      it('should return NewMock for empty Mock initial value', () => {
        const formGroup = service.createMockFormGroup();

        const mock = service.getMock(formGroup) as any;

        expect(mock).toMatchObject({});
      });

      it('should return IMock', () => {
        const formGroup = service.createMockFormGroup(sampleWithRequiredData);

        const mock = service.getMock(formGroup) as any;

        expect(mock).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMock should not enable id FormControl', () => {
        const formGroup = service.createMockFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMock should disable id FormControl', () => {
        const formGroup = service.createMockFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
