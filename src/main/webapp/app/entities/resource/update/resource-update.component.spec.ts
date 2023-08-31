import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ResourceFormService } from './resource-form.service';
import { ResourceService } from '../service/resource.service';
import { IResource } from '../resource.model';
import { IMock } from 'app/entities/mock/mock.model';
import { MockService } from 'app/entities/mock/service/mock.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';

import { ResourceUpdateComponent } from './resource-update.component';

describe('Resource Management Update Component', () => {
  let comp: ResourceUpdateComponent;
  let fixture: ComponentFixture<ResourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resourceFormService: ResourceFormService;
  let resourceService: ResourceService;
  let mockService: MockService;
  let projectService: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ResourceUpdateComponent],
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
      .overrideTemplate(ResourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resourceFormService = TestBed.inject(ResourceFormService);
    resourceService = TestBed.inject(ResourceService);
    mockService = TestBed.inject(MockService);
    projectService = TestBed.inject(ProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Mock query and add missing value', () => {
      const resource: IResource = { id: 'CBA' };
      const mock: IMock = { id: '131129e3-2b49-499d-b1eb-3ec9705ac3b0' };
      resource.mock = mock;

      const mockCollection: IMock[] = [{ id: '0588bd34-c904-49e2-a584-64541be55ddb' }];
      jest.spyOn(mockService, 'query').mockReturnValue(of(new HttpResponse({ body: mockCollection })));
      const additionalMocks = [mock];
      const expectedCollection: IMock[] = [...additionalMocks, ...mockCollection];
      jest.spyOn(mockService, 'addMockToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resource });
      comp.ngOnInit();

      expect(mockService.query).toHaveBeenCalled();
      expect(mockService.addMockToCollectionIfMissing).toHaveBeenCalledWith(
        mockCollection,
        ...additionalMocks.map(expect.objectContaining)
      );
      expect(comp.mocksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Project query and add missing value', () => {
      const resource: IResource = { id: 'CBA' };
      const project: IProject = { id: 'b3cb7b41-0e21-4dae-97c2-2df6f86a3fa4' };
      resource.project = project;

      const projectCollection: IProject[] = [{ id: 'dc00a94b-a7f2-45ac-8ad7-875ff7e215c3' }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [project];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resource });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining)
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resource: IResource = { id: 'CBA' };
      const mock: IMock = { id: 'ad296ffc-2e9e-4767-8fdb-be86b3f448e3' };
      resource.mock = mock;
      const project: IProject = { id: 'bf7d2bdb-f2c2-4483-b125-f01834167e29' };
      resource.project = project;

      activatedRoute.data = of({ resource });
      comp.ngOnInit();

      expect(comp.mocksSharedCollection).toContain(mock);
      expect(comp.projectsSharedCollection).toContain(project);
      expect(comp.resource).toEqual(resource);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResource>>();
      const resource = { id: 'ABC' };
      jest.spyOn(resourceFormService, 'getResource').mockReturnValue(resource);
      jest.spyOn(resourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resource }));
      saveSubject.complete();

      // THEN
      expect(resourceFormService.getResource).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resourceService.update).toHaveBeenCalledWith(expect.objectContaining(resource));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResource>>();
      const resource = { id: 'ABC' };
      jest.spyOn(resourceFormService, 'getResource').mockReturnValue({ id: null });
      jest.spyOn(resourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resource: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resource }));
      saveSubject.complete();

      // THEN
      expect(resourceFormService.getResource).toHaveBeenCalled();
      expect(resourceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResource>>();
      const resource = { id: 'ABC' };
      jest.spyOn(resourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resourceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMock', () => {
      it('Should forward to mockService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(mockService, 'compareMock');
        comp.compareMock(entity, entity2);
        expect(mockService.compareMock).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProject', () => {
      it('Should forward to projectService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(projectService, 'compareProject');
        comp.compareProject(entity, entity2);
        expect(projectService.compareProject).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
