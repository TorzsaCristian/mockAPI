import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EndpointDetailComponent } from './endpoint-detail.component';

describe('Endpoint Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EndpointDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EndpointDetailComponent,
              resolve: { endpoint: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(EndpointDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load endpoint on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EndpointDetailComponent);

      // THEN
      expect(instance.endpoint).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
