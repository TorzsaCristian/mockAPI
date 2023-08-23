import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResourceSchema } from '../resource-schema.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../resource-schema.test-samples';

import { ResourceSchemaService } from './resource-schema.service';

const requireRestSample: IResourceSchema = {
  ...sampleWithRequiredData,
};

describe('ResourceSchema Service', () => {
  let service: ResourceSchemaService;
  let httpMock: HttpTestingController;
  let expectedResult: IResourceSchema | IResourceSchema[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResourceSchemaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ResourceSchema', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const resourceSchema = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(resourceSchema).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ResourceSchema', () => {
      const resourceSchema = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(resourceSchema).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ResourceSchema', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ResourceSchema', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ResourceSchema', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addResourceSchemaToCollectionIfMissing', () => {
      it('should add a ResourceSchema to an empty array', () => {
        const resourceSchema: IResourceSchema = sampleWithRequiredData;
        expectedResult = service.addResourceSchemaToCollectionIfMissing([], resourceSchema);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resourceSchema);
      });

      it('should not add a ResourceSchema to an array that contains it', () => {
        const resourceSchema: IResourceSchema = sampleWithRequiredData;
        const resourceSchemaCollection: IResourceSchema[] = [
          {
            ...resourceSchema,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResourceSchemaToCollectionIfMissing(resourceSchemaCollection, resourceSchema);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ResourceSchema to an array that doesn't contain it", () => {
        const resourceSchema: IResourceSchema = sampleWithRequiredData;
        const resourceSchemaCollection: IResourceSchema[] = [sampleWithPartialData];
        expectedResult = service.addResourceSchemaToCollectionIfMissing(resourceSchemaCollection, resourceSchema);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resourceSchema);
      });

      it('should add only unique ResourceSchema to an array', () => {
        const resourceSchemaArray: IResourceSchema[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const resourceSchemaCollection: IResourceSchema[] = [sampleWithRequiredData];
        expectedResult = service.addResourceSchemaToCollectionIfMissing(resourceSchemaCollection, ...resourceSchemaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resourceSchema: IResourceSchema = sampleWithRequiredData;
        const resourceSchema2: IResourceSchema = sampleWithPartialData;
        expectedResult = service.addResourceSchemaToCollectionIfMissing([], resourceSchema, resourceSchema2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resourceSchema);
        expect(expectedResult).toContain(resourceSchema2);
      });

      it('should accept null and undefined values', () => {
        const resourceSchema: IResourceSchema = sampleWithRequiredData;
        expectedResult = service.addResourceSchemaToCollectionIfMissing([], null, resourceSchema, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resourceSchema);
      });

      it('should return initial array if no ResourceSchema is added', () => {
        const resourceSchemaCollection: IResourceSchema[] = [sampleWithRequiredData];
        expectedResult = service.addResourceSchemaToCollectionIfMissing(resourceSchemaCollection, undefined, null);
        expect(expectedResult).toEqual(resourceSchemaCollection);
      });
    });

    describe('compareResourceSchema', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResourceSchema(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareResourceSchema(entity1, entity2);
        const compareResult2 = service.compareResourceSchema(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareResourceSchema(entity1, entity2);
        const compareResult2 = service.compareResourceSchema(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareResourceSchema(entity1, entity2);
        const compareResult2 = service.compareResourceSchema(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
