import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEndpoint } from '../endpoint.model';
import { EndpointService } from '../service/endpoint.service';

export const endpointResolve = (route: ActivatedRouteSnapshot): Observable<null | IEndpoint> => {
  const id = route.params['id'];
  if (id) {
    return inject(EndpointService)
      .find(id)
      .pipe(
        mergeMap((endpoint: HttpResponse<IEndpoint>) => {
          if (endpoint.body) {
            return of(endpoint.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default endpointResolve;
