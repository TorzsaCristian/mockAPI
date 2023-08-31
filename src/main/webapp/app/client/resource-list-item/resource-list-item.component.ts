import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IResource } from 'app/entities/resource/resource.model';

@Component({
  selector: 'jhi-resource-list-item',
  templateUrl: './resource-list-item.component.html',
  styleUrls: ['./resource-list-item.component.scss']
})
export class ResourceListItemComponent {

  @Input() resource: IResource | null = null;

  @Output() generateResouceEvent = new EventEmitter();
  @Output() editResourceEvent = new EventEmitter();
  @Output() deleteResourceEvent = new EventEmitter();


}
