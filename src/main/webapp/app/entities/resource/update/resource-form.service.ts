import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResource, NewResource } from '../resource.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResource for edit and NewResourceFormGroupInput for create.
 */
type ResourceFormGroupInput = IResource | PartialWithRequiredKeyOf<NewResource>;

type ResourceFormDefaults = Pick<NewResource, 'id'>;

type ResourceFormGroupContent = {
  id: FormControl<IResource['id'] | NewResource['id']>;
  name: FormControl<IResource['name']>;
  generator: FormControl<IResource['generator']>;
  count: FormControl<IResource['count']>;
  mock: FormControl<IResource['mock']>;
  project: FormControl<IResource['project']>;
};

export type ResourceFormGroup = FormGroup<ResourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceFormService {
  createResourceFormGroup(resource: ResourceFormGroupInput = { id: null }): ResourceFormGroup {
    const resourceRawValue = {
      ...this.getFormDefaults(),
      ...resource,
    };
    return new FormGroup<ResourceFormGroupContent>({
      id: new FormControl(
        { value: resourceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(resourceRawValue.name, {
        validators: [Validators.required],
      }),
      generator: new FormControl(resourceRawValue.generator),
      count: new FormControl(resourceRawValue.count),
      mock: new FormControl(resourceRawValue.mock),
      project: new FormControl(resourceRawValue.project),
    });
  }

  getResource(form: ResourceFormGroup): IResource | NewResource {
    return form.getRawValue() as IResource | NewResource;
  }

  resetForm(form: ResourceFormGroup, resource: ResourceFormGroupInput): void {
    const resourceRawValue = { ...this.getFormDefaults(), ...resource };
    form.reset(
      {
        ...resourceRawValue,
        id: { value: resourceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ResourceFormDefaults {
    return {
      id: null,
    };
  }
}
