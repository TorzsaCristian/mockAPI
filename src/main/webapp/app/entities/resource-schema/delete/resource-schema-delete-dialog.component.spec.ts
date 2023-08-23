jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ResourceSchemaService } from '../service/resource-schema.service';

import { ResourceSchemaDeleteDialogComponent } from './resource-schema-delete-dialog.component';

describe('ResourceSchema Management Delete Component', () => {
  let comp: ResourceSchemaDeleteDialogComponent;
  let fixture: ComponentFixture<ResourceSchemaDeleteDialogComponent>;
  let service: ResourceSchemaService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ResourceSchemaDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ResourceSchemaDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ResourceSchemaDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ResourceSchemaService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
