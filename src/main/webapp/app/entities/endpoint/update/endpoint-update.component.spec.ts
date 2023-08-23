import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EndpointFormService } from './endpoint-form.service';
import { EndpointService } from '../service/endpoint.service';
import { IEndpoint } from '../endpoint.model';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';

import { EndpointUpdateComponent } from './endpoint-update.component';

describe('Endpoint Management Update Component', () => {
  let comp: EndpointUpdateComponent;
  let fixture: ComponentFixture<EndpointUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let endpointFormService: EndpointFormService;
  let endpointService: EndpointService;
  let resourceService: ResourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EndpointUpdateComponent],
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
      .overrideTemplate(EndpointUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EndpointUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    endpointFormService = TestBed.inject(EndpointFormService);
    endpointService = TestBed.inject(EndpointService);
    resourceService = TestBed.inject(ResourceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Resource query and add missing value', () => {
      const endpoint: IEndpoint = { id: 'CBA' };
      const resource: IResource = { id: '746014ab-5e4a-4e79-bb02-1e830cd7987b' };
      endpoint.resource = resource;

      const resourceCollection: IResource[] = [{ id: '13e9f517-04cf-4011-add7-b0b6a920135b' }];
      jest.spyOn(resourceService, 'query').mockReturnValue(of(new HttpResponse({ body: resourceCollection })));
      const additionalResources = [resource];
      const expectedCollection: IResource[] = [...additionalResources, ...resourceCollection];
      jest.spyOn(resourceService, 'addResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ endpoint });
      comp.ngOnInit();

      expect(resourceService.query).toHaveBeenCalled();
      expect(resourceService.addResourceToCollectionIfMissing).toHaveBeenCalledWith(
        resourceCollection,
        ...additionalResources.map(expect.objectContaining)
      );
      expect(comp.resourcesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const endpoint: IEndpoint = { id: 'CBA' };
      const resource: IResource = { id: '10bbfc96-8c8b-430b-a209-76fa34317e98' };
      endpoint.resource = resource;

      activatedRoute.data = of({ endpoint });
      comp.ngOnInit();

      expect(comp.resourcesSharedCollection).toContain(resource);
      expect(comp.endpoint).toEqual(endpoint);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndpoint>>();
      const endpoint = { id: 'ABC' };
      jest.spyOn(endpointFormService, 'getEndpoint').mockReturnValue(endpoint);
      jest.spyOn(endpointService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endpoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: endpoint }));
      saveSubject.complete();

      // THEN
      expect(endpointFormService.getEndpoint).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(endpointService.update).toHaveBeenCalledWith(expect.objectContaining(endpoint));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndpoint>>();
      const endpoint = { id: 'ABC' };
      jest.spyOn(endpointFormService, 'getEndpoint').mockReturnValue({ id: null });
      jest.spyOn(endpointService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endpoint: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: endpoint }));
      saveSubject.complete();

      // THEN
      expect(endpointFormService.getEndpoint).toHaveBeenCalled();
      expect(endpointService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndpoint>>();
      const endpoint = { id: 'ABC' };
      jest.spyOn(endpointService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endpoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(endpointService.update).toHaveBeenCalled();
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
