import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import HomeComponent from './home/home.component';
import NavbarComponent from './layouts/navbar/navbar.component';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProjectListComponent } from "./client/project-list/project-list.component";
import { ASC } from './config/navigation.constants';
import { EndpointFormComponent } from './client/endpoint-form/endpoint-form.component';
import { projectResolve } from "./client/project-routing-resolve.service";
import { ProjectDetailsComponent } from './client/project-details/project-details.component';


@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: '',
          component: HomeComponent,
          title: 'home.title',
        },
        {
          path: '',
          component: NavbarComponent,
          outlet: 'navbar',
        },
        {
          path: 'projects',
          component: ProjectListComponent,
          data: {
            defaultSort: 'id,' + ASC,
          },
        },
        {
          path: 'projects/:id/view',
          component: ProjectDetailsComponent,
          resolve: {
            project: projectResolve,
          },
          canActivate: [UserRouteAccessService],
        },
        {
          path: 'endpoints',
          component: EndpointFormComponent,
        },
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module'),
        },
        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(({ EntityRoutingModule }) => EntityRoutingModule),
        },
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED, bindToComponentInputs: true }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule { }
