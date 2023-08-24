import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { SelectItem } from 'primeng/api';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-endpoint-form',
  templateUrl: './endpoint-form.component.html',
  styleUrls: ['./endpoint-form.component.scss']
})
export class EndpointFormComponent implements OnInit, OnDestroy {


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

  constructor(private fb: FormBuilder) {
    this.endpointForm = this.fb.group({
      name: ['', Validators.required],
      generator: ['', Validators.required],
      resourceSchema: this.fb.array([]),
      endpoints: this.fb.array([]),
    });
  }

  ngOnInit(): void {
    this.createDetaulfSchemas();
    this.createDefaultEndpoints();

    this.nameChangeSubscription = this.endpointForm.get('name')!.valueChanges.subscribe(value => {
      // this.createDefaultEndpoints(value);

      this.endpointsArray.controls.forEach(element => {
        element.get('url')!.setValue(`/api/${value as string}`);
      });
    });
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
      response: ''
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'GET',
      url: `/api/${resourceName}/:id`,
      response: ''
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'POST',
      url: `/api/${resourceName}`,
      response: ''
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'PUT',
      url: `/api/${resourceName}/:id`,
      response: ''
    }));
    this.endpointsArray.push(this.fb.group({
      enabled: true,
      method: 'DELETE',
      url: `/api/${resourceName}/:id`,
      response: ''
    }));
  }

  addSchema(): void {
    this.resourceSchemaArray.push(this.fb.group({
      name: '',
      type: this.typeOptions[1].value,
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
    console.warn(this.endpointForm.value);
  }
}
