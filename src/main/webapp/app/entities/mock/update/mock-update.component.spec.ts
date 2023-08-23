import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MockFormService } from './mock-form.service';
import { MockService } from '../service/mock.service';
import { IMock } from '../mock.model';

import { MockUpdateComponent } from './mock-update.component';

describe('Mock Management Update Component', () => {
  let comp: MockUpdateComponent;
  let fixture: ComponentFixture<MockUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mockFormService: MockFormService;
  let mockService: MockService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MockUpdateComponent],
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
      .overrideTemplate(MockUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MockUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mockFormService = TestBed.inject(MockFormService);
    mockService = TestBed.inject(MockService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const mock: IMock = { id: 'CBA' };

      activatedRoute.data = of({ mock });
      comp.ngOnInit();

      expect(comp.mock).toEqual(mock);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMock>>();
      const mock = { id: 'ABC' };
      jest.spyOn(mockFormService, 'getMock').mockReturnValue(mock);
      jest.spyOn(mockService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mock }));
      saveSubject.complete();

      // THEN
      expect(mockFormService.getMock).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mockService.update).toHaveBeenCalledWith(expect.objectContaining(mock));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMock>>();
      const mock = { id: 'ABC' };
      jest.spyOn(mockFormService, 'getMock').mockReturnValue({ id: null });
      jest.spyOn(mockService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mock: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mock }));
      saveSubject.complete();

      // THEN
      expect(mockFormService.getMock).toHaveBeenCalled();
      expect(mockService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMock>>();
      const mock = { id: 'ABC' };
      jest.spyOn(mockService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mockService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
