import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResourceSchema, NewResourceSchema } from '../resource-schema.model';

export type PartialUpdateResourceSchema = Partial<IResourceSchema> & Pick<IResourceSchema, 'id'>;

export type EntityResponseType = HttpResponse<IResourceSchema>;
export type EntityArrayResponseType = HttpResponse<IResourceSchema[]>;

@Injectable({ providedIn: 'root' })
export class ResourceSchemaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resource-schemas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resourceSchema: NewResourceSchema): Observable<EntityResponseType> {
    return this.http.post<IResourceSchema>(this.resourceUrl, resourceSchema, { observe: 'response' });
  }

  update(resourceSchema: IResourceSchema): Observable<EntityResponseType> {
    return this.http.put<IResourceSchema>(`${this.resourceUrl}/${this.getResourceSchemaIdentifier(resourceSchema)}`, resourceSchema, {
      observe: 'response',
    });
  }

  partialUpdate(resourceSchema: PartialUpdateResourceSchema): Observable<EntityResponseType> {
    return this.http.patch<IResourceSchema>(`${this.resourceUrl}/${this.getResourceSchemaIdentifier(resourceSchema)}`, resourceSchema, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IResourceSchema>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResourceSchema[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResourceSchemaIdentifier(resourceSchema: Pick<IResourceSchema, 'id'>): string {
    return resourceSchema.id;
  }

  compareResourceSchema(o1: Pick<IResourceSchema, 'id'> | null, o2: Pick<IResourceSchema, 'id'> | null): boolean {
    return o1 && o2 ? this.getResourceSchemaIdentifier(o1) === this.getResourceSchemaIdentifier(o2) : o1 === o2;
  }

  addResourceSchemaToCollectionIfMissing<Type extends Pick<IResourceSchema, 'id'>>(
    resourceSchemaCollection: Type[],
    ...resourceSchemasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resourceSchemas: Type[] = resourceSchemasToCheck.filter(isPresent);
    if (resourceSchemas.length > 0) {
      const resourceSchemaCollectionIdentifiers = resourceSchemaCollection.map(
        resourceSchemaItem => this.getResourceSchemaIdentifier(resourceSchemaItem)!
      );
      const resourceSchemasToAdd = resourceSchemas.filter(resourceSchemaItem => {
        const resourceSchemaIdentifier = this.getResourceSchemaIdentifier(resourceSchemaItem);
        if (resourceSchemaCollectionIdentifiers.includes(resourceSchemaIdentifier)) {
          return false;
        }
        resourceSchemaCollectionIdentifiers.push(resourceSchemaIdentifier);
        return true;
      });
      return [...resourceSchemasToAdd, ...resourceSchemaCollection];
    }
    return resourceSchemaCollection;
  }
}
