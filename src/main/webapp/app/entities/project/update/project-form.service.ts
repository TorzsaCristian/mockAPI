import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProject, NewProject } from '../project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProject for edit and NewProjectFormGroupInput for create.
 */
type ProjectFormGroupInput = IProject | PartialWithRequiredKeyOf<NewProject>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProject | NewProject> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

type ProjectFormRawValue = FormValueOf<IProject>;

type NewProjectFormRawValue = FormValueOf<NewProject>;

type ProjectFormDefaults = Pick<NewProject, 'id' | 'isPublic' | 'createdAt' | 'collaborators'>;

type ProjectFormGroupContent = {
  id: FormControl<ProjectFormRawValue['id'] | NewProject['id']>;
  resourceTreeId: FormControl<ProjectFormRawValue['resourceTreeId']>;
  name: FormControl<ProjectFormRawValue['name']>;
  prefix: FormControl<ProjectFormRawValue['prefix']>;
  isPublic: FormControl<ProjectFormRawValue['isPublic']>;
  createdAt: FormControl<ProjectFormRawValue['createdAt']>;
  owner: FormControl<ProjectFormRawValue['owner']>;
  collaborators: FormControl<ProjectFormRawValue['collaborators']>;
};

export type ProjectFormGroup = FormGroup<ProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectFormService {
  createProjectFormGroup(project: ProjectFormGroupInput = { id: null }): ProjectFormGroup {
    const projectRawValue = this.convertProjectToProjectRawValue({
      ...this.getFormDefaults(),
      ...project,
    });
    return new FormGroup<ProjectFormGroupContent>({
      id: new FormControl(
        { value: projectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      resourceTreeId: new FormControl(projectRawValue.resourceTreeId),
      name: new FormControl(projectRawValue.name, {
        validators: [Validators.required],
      }),
      prefix: new FormControl(projectRawValue.prefix),
      isPublic: new FormControl(projectRawValue.isPublic),
      createdAt: new FormControl(projectRawValue.createdAt),
      owner: new FormControl(projectRawValue.owner),
      collaborators: new FormControl(projectRawValue.collaborators ?? []),
    });
  }

  getProject(form: ProjectFormGroup): IProject | NewProject {
    return this.convertProjectRawValueToProject(form.getRawValue() as ProjectFormRawValue | NewProjectFormRawValue);
  }

  resetForm(form: ProjectFormGroup, project: ProjectFormGroupInput): void {
    const projectRawValue = this.convertProjectToProjectRawValue({ ...this.getFormDefaults(), ...project });
    form.reset(
      {
        ...projectRawValue,
        id: { value: projectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProjectFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isPublic: false,
      createdAt: currentTime,
      collaborators: [],
    };
  }

  private convertProjectRawValueToProject(rawProject: ProjectFormRawValue | NewProjectFormRawValue): IProject | NewProject {
    return {
      ...rawProject,
      createdAt: dayjs(rawProject.createdAt, DATE_TIME_FORMAT),
    };
  }

  private convertProjectToProjectRawValue(
    project: IProject | (Partial<NewProject> & ProjectFormDefaults)
  ): ProjectFormRawValue | PartialWithRequiredKeyOf<NewProjectFormRawValue> {
    return {
      ...project,
      createdAt: project.createdAt ? project.createdAt.format(DATE_TIME_FORMAT) : undefined,
      collaborators: project.collaborators ?? [],
    };
  }
}
