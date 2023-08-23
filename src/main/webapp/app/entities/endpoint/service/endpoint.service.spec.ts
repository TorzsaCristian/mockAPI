import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEndpoint } from '../endpoint.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../endpoint.test-samples';

import { EndpointService } from './endpoint.service';

const requireRestSample: IEndpoint = {
  ...sampleWithRequiredData,
};

describe('Endpoint Service', () => {
  let service: EndpointService;
  let httpMock: HttpTestingController;
  let expectedResult: IEndpoint | IEndpoint[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EndpointService);
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

    it('should create a Endpoint', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const endpoint = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(endpoint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Endpoint', () => {
      const endpoint = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(endpoint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Endpoint', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Endpoint', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Endpoint', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEndpointToCollectionIfMissing', () => {
      it('should add a Endpoint to an empty array', () => {
        const endpoint: IEndpoint = sampleWithRequiredData;
        expectedResult = service.addEndpointToCollectionIfMissing([], endpoint);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(endpoint);
      });

      it('should not add a Endpoint to an array that contains it', () => {
        const endpoint: IEndpoint = sampleWithRequiredData;
        const endpointCollection: IEndpoint[] = [
          {
            ...endpoint,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEndpointToCollectionIfMissing(endpointCollection, endpoint);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Endpoint to an array that doesn't contain it", () => {
        const endpoint: IEndpoint = sampleWithRequiredData;
        const endpointCollection: IEndpoint[] = [sampleWithPartialData];
        expectedResult = service.addEndpointToCollectionIfMissing(endpointCollection, endpoint);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(endpoint);
      });

      it('should add only unique Endpoint to an array', () => {
        const endpointArray: IEndpoint[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const endpointCollection: IEndpoint[] = [sampleWithRequiredData];
        expectedResult = service.addEndpointToCollectionIfMissing(endpointCollection, ...endpointArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const endpoint: IEndpoint = sampleWithRequiredData;
        const endpoint2: IEndpoint = sampleWithPartialData;
        expectedResult = service.addEndpointToCollectionIfMissing([], endpoint, endpoint2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(endpoint);
        expect(expectedResult).toContain(endpoint2);
      });

      it('should accept null and undefined values', () => {
        const endpoint: IEndpoint = sampleWithRequiredData;
        expectedResult = service.addEndpointToCollectionIfMissing([], null, endpoint, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(endpoint);
      });

      it('should return initial array if no Endpoint is added', () => {
        const endpointCollection: IEndpoint[] = [sampleWithRequiredData];
        expectedResult = service.addEndpointToCollectionIfMissing(endpointCollection, undefined, null);
        expect(expectedResult).toEqual(endpointCollection);
      });
    });

    describe('compareEndpoint', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEndpoint(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareEndpoint(entity1, entity2);
        const compareResult2 = service.compareEndpoint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareEndpoint(entity1, entity2);
        const compareResult2 = service.compareEndpoint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareEndpoint(entity1, entity2);
        const compareResult2 = service.compareEndpoint(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
