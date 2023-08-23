import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EndpointComponent } from './list/endpoint.component';
import { EndpointDetailComponent } from './detail/endpoint-detail.component';
import { EndpointUpdateComponent } from './update/endpoint-update.component';
import EndpointResolve from './route/endpoint-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const endpointRoute: Routes = [
  {
    path: '',
    component: EndpointComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EndpointDetailComponent,
    resolve: {
      endpoint: EndpointResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EndpointUpdateComponent,
    resolve: {
      endpoint: EndpointResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EndpointUpdateComponent,
    resolve: {
      endpoint: EndpointResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default endpointRoute;
