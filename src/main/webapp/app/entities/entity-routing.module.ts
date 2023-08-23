import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'mock',
        data: { pageTitle: 'mockApiApp.mock.home.title' },
        loadChildren: () => import('./mock/mock.routes'),
      },
      {
        path: 'resource-schema',
        data: { pageTitle: 'mockApiApp.resourceSchema.home.title' },
        loadChildren: () => import('./resource-schema/resource-schema.routes'),
      },
      {
        path: 'project',
        data: { pageTitle: 'mockApiApp.project.home.title' },
        loadChildren: () => import('./project/project.routes'),
      },
      {
        path: 'endpoint',
        data: { pageTitle: 'mockApiApp.endpoint.home.title' },
        loadChildren: () => import('./endpoint/endpoint.routes'),
      },
      {
        path: 'resource',
        data: { pageTitle: 'mockApiApp.resource.home.title' },
        loadChildren: () => import('./resource/resource.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
