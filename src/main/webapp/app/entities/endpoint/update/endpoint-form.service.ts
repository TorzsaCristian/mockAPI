import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEndpoint, NewEndpoint } from '../endpoint.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEndpoint for edit and NewEndpointFormGroupInput for create.
 */
type EndpointFormGroupInput = IEndpoint | PartialWithRequiredKeyOf<NewEndpoint>;

type EndpointFormDefaults = Pick<NewEndpoint, 'id' | 'enabled'>;

type EndpointFormGroupContent = {
  id: FormControl<IEndpoint['id'] | NewEndpoint['id']>;
  url: FormControl<IEndpoint['url']>;
  method: FormControl<IEndpoint['method']>;
  enabled: FormControl<IEndpoint['enabled']>;
  response: FormControl<IEndpoint['response']>;
  resource: FormControl<IEndpoint['resource']>;
};

export type EndpointFormGroup = FormGroup<EndpointFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EndpointFormService {
  createEndpointFormGroup(endpoint: EndpointFormGroupInput = { id: null }): EndpointFormGroup {
    const endpointRawValue = {
      ...this.getFormDefaults(),
      ...endpoint,
    };
    return new FormGroup<EndpointFormGroupContent>({
      id: new FormControl(
        { value: endpointRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      url: new FormControl(endpointRawValue.url, {
        validators: [Validators.required],
      }),
      method: new FormControl(endpointRawValue.method, {
        validators: [Validators.required],
      }),
      enabled: new FormControl(endpointRawValue.enabled),
      response: new FormControl(endpointRawValue.response),
      resource: new FormControl(endpointRawValue.resource),
    });
  }

  getEndpoint(form: EndpointFormGroup): IEndpoint | NewEndpoint {
    return form.getRawValue() as IEndpoint | NewEndpoint;
  }

  resetForm(form: EndpointFormGroup, endpoint: EndpointFormGroupInput): void {
    const endpointRawValue = { ...this.getFormDefaults(), ...endpoint };
    form.reset(
      {
        ...endpointRawValue,
        id: { value: endpointRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EndpointFormDefaults {
    return {
      id: null,
      enabled: false,
    };
  }
}
