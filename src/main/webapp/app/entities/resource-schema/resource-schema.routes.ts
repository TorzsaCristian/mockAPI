import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResourceSchemaComponent } from './list/resource-schema.component';
import { ResourceSchemaDetailComponent } from './detail/resource-schema-detail.component';
import { ResourceSchemaUpdateComponent } from './update/resource-schema-update.component';
import ResourceSchemaResolve from './route/resource-schema-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const resourceSchemaRoute: Routes = [
  {
    path: '',
    component: ResourceSchemaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResourceSchemaDetailComponent,
    resolve: {
      resourceSchema: ResourceSchemaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResourceSchemaUpdateComponent,
    resolve: {
      resourceSchema: ResourceSchemaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResourceSchemaUpdateComponent,
    resolve: {
      resourceSchema: ResourceSchemaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default resourceSchemaRoute;
