import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMock, NewMock } from '../mock.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMock for edit and NewMockFormGroupInput for create.
 */
type MockFormGroupInput = IMock | PartialWithRequiredKeyOf<NewMock>;

type MockFormDefaults = Pick<NewMock, 'id'>;

type MockFormGroupContent = {
  id: FormControl<IMock['id'] | NewMock['id']>;
  prefix: FormControl<IMock['prefix']>;
  version: FormControl<IMock['version']>;
};

export type MockFormGroup = FormGroup<MockFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MockFormService {
  createMockFormGroup(mock: MockFormGroupInput = { id: null }): MockFormGroup {
    const mockRawValue = {
      ...this.getFormDefaults(),
      ...mock,
    };
    return new FormGroup<MockFormGroupContent>({
      id: new FormControl(
        { value: mockRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      prefix: new FormControl(mockRawValue.prefix),
      version: new FormControl(mockRawValue.version),
    });
  }

  getMock(form: MockFormGroup): IMock | NewMock {
    return form.getRawValue() as IMock | NewMock;
  }

  resetForm(form: MockFormGroup, mock: MockFormGroupInput): void {
    const mockRawValue = { ...this.getFormDefaults(), ...mock };
    form.reset(
      {
        ...mockRawValue,
        id: { value: mockRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MockFormDefaults {
    return {
      id: null,
    };
  }
}
