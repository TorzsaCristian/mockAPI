import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ResourceSchemaDetailComponent } from './resource-schema-detail.component';

describe('ResourceSchema Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResourceSchemaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ResourceSchemaDetailComponent,
              resolve: { resourceSchema: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ResourceSchemaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load resourceSchema on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResourceSchemaDetailComponent);

      // THEN
      expect(instance.resourceSchema).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
