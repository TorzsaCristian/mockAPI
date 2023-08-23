import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EndpointFormService, EndpointFormGroup } from './endpoint-form.service';
import { IEndpoint } from '../endpoint.model';
import { EndpointService } from '../service/endpoint.service';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';

@Component({
  standalone: true,
  selector: 'jhi-endpoint-update',
  templateUrl: './endpoint-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EndpointUpdateComponent implements OnInit {
  isSaving = false;
  endpoint: IEndpoint | null = null;

  resourcesSharedCollection: IResource[] = [];

  editForm: EndpointFormGroup = this.endpointFormService.createEndpointFormGroup();

  constructor(
    protected endpointService: EndpointService,
    protected endpointFormService: EndpointFormService,
    protected resourceService: ResourceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResource = (o1: IResource | null, o2: IResource | null): boolean => this.resourceService.compareResource(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endpoint }) => {
      this.endpoint = endpoint;
      if (endpoint) {
        this.updateForm(endpoint);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const endpoint = this.endpointFormService.getEndpoint(this.editForm);
    if (endpoint.id !== null) {
      this.subscribeToSaveResponse(this.endpointService.update(endpoint));
    } else {
      this.subscribeToSaveResponse(this.endpointService.create(endpoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndpoint>>): void {
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

  protected updateForm(endpoint: IEndpoint): void {
    this.endpoint = endpoint;
    this.endpointFormService.resetForm(this.editForm, endpoint);

    this.resourcesSharedCollection = this.resourceService.addResourceToCollectionIfMissing<IResource>(
      this.resourcesSharedCollection,
      endpoint.resource
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resourceService
      .query()
      .pipe(map((res: HttpResponse<IResource[]>) => res.body ?? []))
      .pipe(
        map((resources: IResource[]) =>
          this.resourceService.addResourceToCollectionIfMissing<IResource>(resources, this.endpoint?.resource)
        )
      )
      .subscribe((resources: IResource[]) => (this.resourcesSharedCollection = resources));
  }
}
