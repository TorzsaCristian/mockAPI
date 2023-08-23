import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MockComponent } from './list/mock.component';
import { MockDetailComponent } from './detail/mock-detail.component';
import { MockUpdateComponent } from './update/mock-update.component';
import MockResolve from './route/mock-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const mockRoute: Routes = [
  {
    path: '',
    component: MockComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MockDetailComponent,
    resolve: {
      mock: MockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MockUpdateComponent,
    resolve: {
      mock: MockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MockUpdateComponent,
    resolve: {
      mock: MockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mockRoute;
