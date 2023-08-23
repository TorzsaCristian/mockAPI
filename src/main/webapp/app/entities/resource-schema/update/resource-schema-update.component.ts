import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ResourceSchemaFormService, ResourceSchemaFormGroup } from './resource-schema-form.service';
import { IResourceSchema } from '../resource-schema.model';
import { ResourceSchemaService } from '../service/resource-schema.service';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';

@Component({
  standalone: true,
  selector: 'jhi-resource-schema-update',
  templateUrl: './resource-schema-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResourceSchemaUpdateComponent implements OnInit {
  isSaving = false;
  resourceSchema: IResourceSchema | null = null;

  resourcesSharedCollection: IResource[] = [];

  editForm: ResourceSchemaFormGroup = this.resourceSchemaFormService.createResourceSchemaFormGroup();

  constructor(
    protected resourceSchemaService: ResourceSchemaService,
    protected resourceSchemaFormService: ResourceSchemaFormService,
    protected resourceService: ResourceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResource = (o1: IResource | null, o2: IResource | null): boolean => this.resourceService.compareResource(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourceSchema }) => {
      this.resourceSchema = resourceSchema;
      if (resourceSchema) {
        this.updateForm(resourceSchema);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resourceSchema = this.resourceSchemaFormService.getResourceSchema(this.editForm);
    if (resourceSchema.id !== null) {
      this.subscribeToSaveResponse(this.resourceSchemaService.update(resourceSchema));
    } else {
      this.subscribeToSaveResponse(this.resourceSchemaService.create(resourceSchema));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceSchema>>): void {
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

  protected updateForm(resourceSchema: IResourceSchema): void {
    this.resourceSchema = resourceSchema;
    this.resourceSchemaFormService.resetForm(this.editForm, resourceSchema);

    this.resourcesSharedCollection = this.resourceService.addResourceToCollectionIfMissing<IResource>(
      this.resourcesSharedCollection,
      resourceSchema.resource
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resourceService
      .query()
      .pipe(map((res: HttpResponse<IResource[]>) => res.body ?? []))
      .pipe(
        map((resources: IResource[]) =>
          this.resourceService.addResourceToCollectionIfMissing<IResource>(resources, this.resourceSchema?.resource)
        )
      )
      .subscribe((resources: IResource[]) => (this.resourcesSharedCollection = resources));
  }
}
