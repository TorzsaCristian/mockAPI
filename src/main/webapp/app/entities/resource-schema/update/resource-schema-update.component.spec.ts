import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ResourceSchemaFormService } from './resource-schema-form.service';
import { ResourceSchemaService } from '../service/resource-schema.service';
import { IResourceSchema } from '../resource-schema.model';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';

import { ResourceSchemaUpdateComponent } from './resource-schema-update.component';

describe('ResourceSchema Management Update Component', () => {
  let comp: ResourceSchemaUpdateComponent;
  let fixture: ComponentFixture<ResourceSchemaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resourceSchemaFormService: ResourceSchemaFormService;
  let resourceSchemaService: ResourceSchemaService;
  let resourceService: ResourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ResourceSchemaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ResourceSchemaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResourceSchemaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resourceSchemaFormService = TestBed.inject(ResourceSchemaFormService);
    resourceSchemaService = TestBed.inject(ResourceSchemaService);
    resourceService = TestBed.inject(ResourceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Resource query and add missing value', () => {
      const resourceSchema: IResourceSchema = { id: 'CBA' };
      const resource: IResource = { id: 'ad2b09bd-13eb-440b-ba90-d335ea5fcab3' };
      resourceSchema.resource = resource;

      const resourceCollection: IResource[] = [{ id: 'dbc304e4-d8b7-4739-81c8-d5a7a7f3092d' }];
      jest.spyOn(resourceService, 'query').mockReturnValue(of(new HttpResponse({ body: resourceCollection })));
      const additionalResources = [resource];
      const expectedCollection: IResource[] = [...additionalResources, ...resourceCollection];
      jest.spyOn(resourceService, 'addResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resourceSchema });
      comp.ngOnInit();

      expect(resourceService.query).toHaveBeenCalled();
      expect(resourceService.addResourceToCollectionIfMissing).toHaveBeenCalledWith(
        resourceCollection,
        ...additionalResources.map(expect.objectContaining)
      );
      expect(comp.resourcesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resourceSchema: IResourceSchema = { id: 'CBA' };
      const resource: IResource = { id: 'df18d316-ef01-4696-8f77-77c178551872' };
      resourceSchema.resource = resource;

      activatedRoute.data = of({ resourceSchema });
      comp.ngOnInit();

      expect(comp.resourcesSharedCollection).toContain(resource);
      expect(comp.resourceSchema).toEqual(resourceSchema);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceSchema>>();
      const resourceSchema = { id: 'ABC' };
      jest.spyOn(resourceSchemaFormService, 'getResourceSchema').mockReturnValue(resourceSchema);
      jest.spyOn(resourceSchemaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceSchema });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceSchema }));
      saveSubject.complete();

      // THEN
      expect(resourceSchemaFormService.getResourceSchema).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resourceSchemaService.update).toHaveBeenCalledWith(expect.objectContaining(resourceSchema));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceSchema>>();
      const resourceSchema = { id: 'ABC' };
      jest.spyOn(resourceSchemaFormService, 'getResourceSchema').mockReturnValue({ id: null });
      jest.spyOn(resourceSchemaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceSchema: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceSchema }));
      saveSubject.complete();

      // THEN
      expect(resourceSchemaFormService.getResourceSchema).toHaveBeenCalled();
      expect(resourceSchemaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceSchema>>();
      const resourceSchema = { id: 'ABC' };
      jest.spyOn(resourceSchemaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceSchema });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resourceSchemaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareResource', () => {
      it('Should forward to resourceService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(resourceService, 'compareResource');
        comp.compareResource(entity, entity2);
        expect(resourceService.compareResource).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
