import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMock, NewMock } from '../mock.model';

export type PartialUpdateMock = Partial<IMock> & Pick<IMock, 'id'>;

export type EntityResponseType = HttpResponse<IMock>;
export type EntityArrayResponseType = HttpResponse<IMock[]>;

@Injectable({ providedIn: 'root' })
export class MockService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mocks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mock: NewMock): Observable<EntityResponseType> {
    return this.http.post<IMock>(this.resourceUrl, mock, { observe: 'response' });
  }

  update(mock: IMock): Observable<EntityResponseType> {
    return this.http.put<IMock>(`${this.resourceUrl}/${this.getMockIdentifier(mock)}`, mock, { observe: 'response' });
  }

  partialUpdate(mock: PartialUpdateMock): Observable<EntityResponseType> {
    return this.http.patch<IMock>(`${this.resourceUrl}/${this.getMockIdentifier(mock)}`, mock, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IMock>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMock[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMockIdentifier(mock: Pick<IMock, 'id'>): string {
    return mock.id;
  }

  compareMock(o1: Pick<IMock, 'id'> | null, o2: Pick<IMock, 'id'> | null): boolean {
    return o1 && o2 ? this.getMockIdentifier(o1) === this.getMockIdentifier(o2) : o1 === o2;
  }

  addMockToCollectionIfMissing<Type extends Pick<IMock, 'id'>>(
    mockCollection: Type[],
    ...mocksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mocks: Type[] = mocksToCheck.filter(isPresent);
    if (mocks.length > 0) {
      const mockCollectionIdentifiers = mockCollection.map(mockItem => this.getMockIdentifier(mockItem)!);
      const mocksToAdd = mocks.filter(mockItem => {
        const mockIdentifier = this.getMockIdentifier(mockItem);
        if (mockCollectionIdentifiers.includes(mockIdentifier)) {
          return false;
        }
        mockCollectionIdentifiers.push(mockIdentifier);
        return true;
      });
      return [...mocksToAdd, ...mockCollection];
    }
    return mockCollection;
  }
}
