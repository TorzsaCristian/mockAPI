import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MockFormService, MockFormGroup } from './mock-form.service';
import { IMock } from '../mock.model';
import { MockService } from '../service/mock.service';

@Component({
  standalone: true,
  selector: 'jhi-mock-update',
  templateUrl: './mock-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MockUpdateComponent implements OnInit {
  isSaving = false;
  mock: IMock | null = null;

  editForm: MockFormGroup = this.mockFormService.createMockFormGroup();

  constructor(protected mockService: MockService, protected mockFormService: MockFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mock }) => {
      this.mock = mock;
      if (mock) {
        this.updateForm(mock);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mock = this.mockFormService.getMock(this.editForm);
    if (mock.id !== null) {
      this.subscribeToSaveResponse(this.mockService.update(mock));
    } else {
      this.subscribeToSaveResponse(this.mockService.create(mock));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMock>>): void {
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

  protected updateForm(mock: IMock): void {
    this.mock = mock;
    this.mockFormService.resetForm(this.editForm, mock);
  }
}
