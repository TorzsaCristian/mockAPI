import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMock } from '../mock.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../mock.test-samples';

import { MockService } from './mock.service';

const requireRestSample: IMock = {
  ...sampleWithRequiredData,
};

describe('Mock Service', () => {
  let service: MockService;
  let httpMock: HttpTestingController;
  let expectedResult: IMock | IMock[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MockService);
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

    it('should create a Mock', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const mock = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mock).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mock', () => {
      const mock = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mock).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mock', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mock', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Mock', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMockToCollectionIfMissing', () => {
      it('should add a Mock to an empty array', () => {
        const mock: IMock = sampleWithRequiredData;
        expectedResult = service.addMockToCollectionIfMissing([], mock);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mock);
      });

      it('should not add a Mock to an array that contains it', () => {
        const mock: IMock = sampleWithRequiredData;
        const mockCollection: IMock[] = [
          {
            ...mock,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMockToCollectionIfMissing(mockCollection, mock);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mock to an array that doesn't contain it", () => {
        const mock: IMock = sampleWithRequiredData;
        const mockCollection: IMock[] = [sampleWithPartialData];
        expectedResult = service.addMockToCollectionIfMissing(mockCollection, mock);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mock);
      });

      it('should add only unique Mock to an array', () => {
        const mockArray: IMock[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mockCollection: IMock[] = [sampleWithRequiredData];
        expectedResult = service.addMockToCollectionIfMissing(mockCollection, ...mockArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mock: IMock = sampleWithRequiredData;
        const mock2: IMock = sampleWithPartialData;
        expectedResult = service.addMockToCollectionIfMissing([], mock, mock2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mock);
        expect(expectedResult).toContain(mock2);
      });

      it('should accept null and undefined values', () => {
        const mock: IMock = sampleWithRequiredData;
        expectedResult = service.addMockToCollectionIfMissing([], null, mock, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mock);
      });

      it('should return initial array if no Mock is added', () => {
        const mockCollection: IMock[] = [sampleWithRequiredData];
        expectedResult = service.addMockToCollectionIfMissing(mockCollection, undefined, null);
        expect(expectedResult).toEqual(mockCollection);
      });
    });

    describe('compareMock', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMock(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareMock(entity1, entity2);
        const compareResult2 = service.compareMock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareMock(entity1, entity2);
        const compareResult2 = service.compareMock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareMock(entity1, entity2);
        const compareResult2 = service.compareMock(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
