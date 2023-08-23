import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMock } from '../mock.model';
import { MockService } from '../service/mock.service';

export const mockResolve = (route: ActivatedRouteSnapshot): Observable<null | IMock> => {
  const id = route.params['id'];
  if (id) {
    return inject(MockService)
      .find(id)
      .pipe(
        mergeMap((mock: HttpResponse<IMock>) => {
          if (mock.body) {
            return of(mock.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default mockResolve;
