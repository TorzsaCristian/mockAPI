import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEndpoint, NewEndpoint } from '../endpoint.model';

export type PartialUpdateEndpoint = Partial<IEndpoint> & Pick<IEndpoint, 'id'>;

export type EntityResponseType = HttpResponse<IEndpoint>;
export type EntityArrayResponseType = HttpResponse<IEndpoint[]>;

@Injectable({ providedIn: 'root' })
export class EndpointService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/endpoints');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(endpoint: NewEndpoint): Observable<EntityResponseType> {
    return this.http.post<IEndpoint>(this.resourceUrl, endpoint, { observe: 'response' });
  }

  update(endpoint: IEndpoint): Observable<EntityResponseType> {
    return this.http.put<IEndpoint>(`${this.resourceUrl}/${this.getEndpointIdentifier(endpoint)}`, endpoint, { observe: 'response' });
  }

  partialUpdate(endpoint: PartialUpdateEndpoint): Observable<EntityResponseType> {
    return this.http.patch<IEndpoint>(`${this.resourceUrl}/${this.getEndpointIdentifier(endpoint)}`, endpoint, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IEndpoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEndpoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEndpointIdentifier(endpoint: Pick<IEndpoint, 'id'>): string {
    return endpoint.id;
  }

  compareEndpoint(o1: Pick<IEndpoint, 'id'> | null, o2: Pick<IEndpoint, 'id'> | null): boolean {
    return o1 && o2 ? this.getEndpointIdentifier(o1) === this.getEndpointIdentifier(o2) : o1 === o2;
  }

  addEndpointToCollectionIfMissing<Type extends Pick<IEndpoint, 'id'>>(
    endpointCollection: Type[],
    ...endpointsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const endpoints: Type[] = endpointsToCheck.filter(isPresent);
    if (endpoints.length > 0) {
      const endpointCollectionIdentifiers = endpointCollection.map(endpointItem => this.getEndpointIdentifier(endpointItem)!);
      const endpointsToAdd = endpoints.filter(endpointItem => {
        const endpointIdentifier = this.getEndpointIdentifier(endpointItem);
        if (endpointCollectionIdentifiers.includes(endpointIdentifier)) {
          return false;
        }
        endpointCollectionIdentifiers.push(endpointIdentifier);
        return true;
      });
      return [...endpointsToAdd, ...endpointCollection];
    }
    return endpointCollection;
  }
}
