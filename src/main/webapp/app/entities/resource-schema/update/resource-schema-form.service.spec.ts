import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../resource-schema.test-samples';

import { ResourceSchemaFormService } from './resource-schema-form.service';

describe('ResourceSchema Form Service', () => {
  let service: ResourceSchemaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResourceSchemaFormService);
  });

  describe('Service methods', () => {
    describe('createResourceSchemaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResourceSchemaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            fakerMethod: expect.any(Object),
            resource: expect.any(Object),
          })
        );
      });

      it('passing IResourceSchema should create a new form with FormGroup', () => {
        const formGroup = service.createResourceSchemaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            fakerMethod: expect.any(Object),
            resource: expect.any(Object),
          })
        );
      });
    });

    describe('getResourceSchema', () => {
      it('should return NewResourceSchema for default ResourceSchema initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createResourceSchemaFormGroup(sampleWithNewData);

        const resourceSchema = service.getResourceSchema(formGroup) as any;

        expect(resourceSchema).toMatchObject(sampleWithNewData);
      });

      it('should return NewResourceSchema for empty ResourceSchema initial value', () => {
        const formGroup = service.createResourceSchemaFormGroup();

        const resourceSchema = service.getResourceSchema(formGroup) as any;

        expect(resourceSchema).toMatchObject({});
      });

      it('should return IResourceSchema', () => {
        const formGroup = service.createResourceSchemaFormGroup(sampleWithRequiredData);

        const resourceSchema = service.getResourceSchema(formGroup) as any;

        expect(resourceSchema).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResourceSchema should not enable id FormControl', () => {
        const formGroup = service.createResourceSchemaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResourceSchema should disable id FormControl', () => {
        const formGroup = service.createResourceSchemaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
