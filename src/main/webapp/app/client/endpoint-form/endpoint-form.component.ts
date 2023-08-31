import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators, ValidationErrors } from '@angular/forms';
import { SelectItem } from 'primeng/api';
import { Subscription } from 'rxjs';
import { MockService } from '../mock.service';
import { IResource } from 'app/entities/resource/resource.model';

@Component({
  selector: 'jhi-endpoint-form',
  templateUrl: './endpoint-form.component.html',
  styleUrls: ['./endpoint-form.component.scss']
})
export class EndpointFormComponent implements OnInit, OnDestroy, OnChanges {


  @Input() resource: IResource | null = null;
  @Input() projectId: string | null = null;

  @Output() submitFormEvent = new EventEmitter();



  typeOptions: SelectItem[] = [
    { label: 'Faker.js', value: 'Faker.js' },
    { label: 'String', value: 'String' },
    { label: 'Number', value: 'Number' },
    { label: 'Boolean', value: 'Boolean' },
    { label: 'Object', value: 'Object' },
    { label: 'Array', value: 'Array' },
    { label: 'Date', value: 'Date' },
  ];


  fakerMethodOptions: SelectItem[] = [
    { label: 'name.fullName', value: 'name.fullName' },
    { label: 'address.city', value: 'address.city' },
    { label: 'address.buildingNumber', value: 'address.buildingNumber' },
    { label: 'commerce.productMaterial', value: 'commerce.productMaterial' },
    { label: 'date.weekday', value: 'date.weekday' }
  ];

  endpointForm: FormGroup;

  nameChangeSubscription: Subscription | undefined = undefined;

  constructor(private mockService: MockService, private fb: FormBuilder, private changeDetector: ChangeDetectorRef) {
    this.endpointForm = this.fb.group({
      name: ['', Validators.required],
      generator: [''],
      resourceSchema: this.fb.array([]),
      endpoints: this.fb.array([]),
    });
  }

  ngOnInit(): void {

    this.nameChangeSubscription = this.endpointForm.get('name')!.valueChanges.subscribe(value => {
      this.endpointsArray.controls.forEach(element => {
        const urlControl = element.get('url');
        if (urlControl) {
          const currentValue = urlControl.value;
          if (typeof currentValue === 'string') {
            urlControl.setValue(this.replaceAfterSecondSlash(currentValue, value));
          }
        }
      });
    });

    if (this.resource) {
      console.warn(this.resource);
      this.patchForm();
    }
  }
  // Refresh if input changes
  ngOnChanges(): void {
    if (this.resource) {
      this.endpointsArray.clear();
      this.resourceSchemaArray.clear();
      this.endpointForm.reset();
      this.patchForm();
    } else {
      this.endpointsArray.clear();
      this.resourceSchemaArray.clear();
      this.endpointForm.reset();
      this.createDetaulfSchemas();
      this.createDefaultEndpoints();
    }
  }

  patchForm(): void {

    this.resourceSchemaArray.push(this.fb.group({
      name: 'id',
      type: 'Object ID'
    }));
    this.endpointForm.patchValue({
      name: this.resource?.name,
      generator: this.resource?.generator,
    });
    this.resource?.resourceSchemas?.forEach(element => {
      this.resourceSchemaArray.push(this.fb.group({
        name: element.name,
        type: element.type,
        fakerMethod: element.fakerMethod,
      }));
    });
    this.changeDetector.detectChanges();
  }

  ngOnDestroy(): void {
    if (this.nameChangeSubscription) {
      this.nameChangeSubscription.unsubscribe();
    }
  }

  get resourceSchemaArray(): FormArray {
    return this.endpointForm.get('resourceSchema') as FormArray;
  }

  get endpointsArray(): FormArray {
    return this.endpointForm.get('endpoints') as FormArray;
  }


  createDetaulfSchemas(): void {
    this.resourceSchemaArray.push(this.fb.group({
      name: 'id',
      type: 'Object ID'
    }));
    this.resourceSchemaArray.push(this.fb.group({
      name: 'name',
      type: this.typeOptions[0].value,
      fakerMethod: this.fakerMethodOptions[0].value
    }));
    this.resourceSchemaArray.push(this.fb.group({
      name: 'address',
      type: this.typeOptions[0].value,
      fakerMethod: this.fakerMethodOptions[1].value
    }));

    this.resourceSchemaArray.push(this.fb.group({
      name: 'message',
      type: this.typeOptions[1].value
    }));
  }

  createDefaultEndpoints(resourceName = '...'): void {
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'GET',
      url: `/api/${resourceName}`,
      response: '$mockData'
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'GET',
      url: `/api/${resourceName}/:id`,
      response: '$mockData'
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'POST',
      url: `/api/${resourceName}`,
      response: '$mockData'
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'PUT',
      url: `/api/${resourceName}/:id`,
      response: '$mockData'
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'DELETE',
      url: `/api/${resourceName}/:id`,
      response: '$mockData'
    }));
  }



  addSchema(): void {
    this.resourceSchemaArray.push(this.fb.group({
      name: ['', Validators.required],
      type: this.typeOptions[1].value,
      fakerMethod: undefined,
    }));
  }

  removeSchema(index: number): void {
    if (this.resourceSchemaArray.length > 1) {
      this.resourceSchemaArray.removeAt(index);
    }
  }

  addEndpoint(): void {
    this.endpointsArray.push(this.fb.group({
      enabled: Boolean,
      method: '',
      url: '',
      response: ''
    }));
  }

  removeEndpoint(index: number): void {
    this.endpointsArray.removeAt(index);
  }

  submit(): void {
    if (!this.endpointForm.invalid) {
      console.warn(this.endpointForm.value);
      this.mockService.sendData(this.projectId!, this.endpointForm.value).subscribe(
        res => {
          console.warn(res);
          this.submitFormEvent.emit();
        },
        err => {
          console.error(err);
        }
      );
    } else {
      console.warn("form invalid");
      this.getFormValidationErrors();
    }
  }

  replaceAfterSecondSlash(str: string, replacement: string): string {
    const parts = str.split('/');

    // Check if there are at least 3 parts and replace the third part.
    if (parts.length > 2) {
      parts[2] = replacement;
    }

    return parts.join('/');
  }

  getFormValidationErrors(): void {
    Object.keys(this.endpointForm.controls).forEach(key => {
      const controlErrors: ValidationErrors | null | undefined = this.endpointForm.get(key)?.errors;
      if (controlErrors != null) {
        Object.keys(controlErrors).forEach(keyError => {
          console.warn('Key control: ' + key + ', keyError: ' + keyError + ', err value: ', controlErrors[keyError]);
        });
      }
    });
  }
}
