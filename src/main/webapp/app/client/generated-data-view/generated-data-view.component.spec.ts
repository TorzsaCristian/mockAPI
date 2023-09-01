import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneratedDataViewComponent } from './generated-data-view.component';

describe('GeneratedDataViewComponent', () => {
  let component: GeneratedDataViewComponent;
  let fixture: ComponentFixture<GeneratedDataViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GeneratedDataViewComponent]
    });
    fixture = TestBed.createComponent(GeneratedDataViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
