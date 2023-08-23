import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResourceSchema, NewResourceSchema } from '../resource-schema.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResourceSchema for edit and NewResourceSchemaFormGroupInput for create.
 */
type ResourceSchemaFormGroupInput = IResourceSchema | PartialWithRequiredKeyOf<NewResourceSchema>;

type ResourceSchemaFormDefaults = Pick<NewResourceSchema, 'id'>;

type ResourceSchemaFormGroupContent = {
  id: FormControl<IResourceSchema['id'] | NewResourceSchema['id']>;
  name: FormControl<IResourceSchema['name']>;
  type: FormControl<IResourceSchema['type']>;
  fakerMethod: FormControl<IResourceSchema['fakerMethod']>;
  resource: FormControl<IResourceSchema['resource']>;
};

export type ResourceSchemaFormGroup = FormGroup<ResourceSchemaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceSchemaFormService {
  createResourceSchemaFormGroup(resourceSchema: ResourceSchemaFormGroupInput = { id: null }): ResourceSchemaFormGroup {
    const resourceSchemaRawValue = {
      ...this.getFormDefaults(),
      ...resourceSchema,
    };
    return new FormGroup<ResourceSchemaFormGroupContent>({
      id: new FormControl(
        { value: resourceSchemaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(resourceSchemaRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(resourceSchemaRawValue.type, {
        validators: [Validators.required],
      }),
      fakerMethod: new FormControl(resourceSchemaRawValue.fakerMethod),
      resource: new FormControl(resourceSchemaRawValue.resource),
    });
  }

  getResourceSchema(form: ResourceSchemaFormGroup): IResourceSchema | NewResourceSchema {
    return form.getRawValue() as IResourceSchema | NewResourceSchema;
  }

  resetForm(form: ResourceSchemaFormGroup, resourceSchema: ResourceSchemaFormGroupInput): void {
    const resourceSchemaRawValue = { ...this.getFormDefaults(), ...resourceSchema };
    form.reset(
      {
        ...resourceSchemaRawValue,
        id: { value: resourceSchemaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ResourceSchemaFormDefaults {
    return {
      id: null,
    };
  }
}
