import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourceSchema } from '../resource-schema.model';
import { ResourceSchemaService } from '../service/resource-schema.service';

export const resourceSchemaResolve = (route: ActivatedRouteSnapshot): Observable<null | IResourceSchema> => {
  const id = route.params['id'];
  if (id) {
    return inject(ResourceSchemaService)
      .find(id)
      .pipe(
        mergeMap((resourceSchema: HttpResponse<IResourceSchema>) => {
          if (resourceSchema.body) {
            return of(resourceSchema.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default resourceSchemaResolve;
