import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MockDetailComponent } from './mock-detail.component';

describe('Mock Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MockDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MockDetailComponent,
              resolve: { mock: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(MockDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load mock on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MockDetailComponent);

      // THEN
      expect(instance.mock).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
