import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ResourceFormService, ResourceFormGroup } from './resource-form.service';
import { IResource } from '../resource.model';
import { ResourceService } from '../service/resource.service';
import { IMock } from 'app/entities/mock/mock.model';
import { MockService } from 'app/entities/mock/service/mock.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';

@Component({
  standalone: true,
  selector: 'jhi-resource-update',
  templateUrl: './resource-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResourceUpdateComponent implements OnInit {
  isSaving = false;
  resource: IResource | null = null;

  mocksSharedCollection: IMock[] = [];
  projectsSharedCollection: IProject[] = [];

  editForm: ResourceFormGroup = this.resourceFormService.createResourceFormGroup();

  constructor(
    protected resourceService: ResourceService,
    protected resourceFormService: ResourceFormService,
    protected mockService: MockService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMock = (o1: IMock | null, o2: IMock | null): boolean => this.mockService.compareMock(o1, o2);

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resource }) => {
      this.resource = resource;
      if (resource) {
        this.updateForm(resource);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resource = this.resourceFormService.getResource(this.editForm);
    if (resource.id !== null) {
      this.subscribeToSaveResponse(this.resourceService.update(resource));
    } else {
      this.subscribeToSaveResponse(this.resourceService.create(resource));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResource>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(resource: IResource): void {
    this.resource = resource;
    this.resourceFormService.resetForm(this.editForm, resource);

    this.mocksSharedCollection = this.mockService.addMockToCollectionIfMissing<IMock>(this.mocksSharedCollection, resource.mock);
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      resource.project
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mockService
      .query()
      .pipe(map((res: HttpResponse<IMock[]>) => res.body ?? []))
      .pipe(map((mocks: IMock[]) => this.mockService.addMockToCollectionIfMissing<IMock>(mocks, this.resource?.mock)))
      .subscribe((mocks: IMock[]) => (this.mocksSharedCollection = mocks));

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.resource?.project)))
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }
}
