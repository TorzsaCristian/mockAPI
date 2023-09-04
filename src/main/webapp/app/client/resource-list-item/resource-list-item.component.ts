import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'jhi-resource-list-item',
  templateUrl: './resource-list-item.component.html',
  styleUrls: ['./resource-list-item.component.scss']
})
export class ResourceListItemComponent {

  @Input() name!: string;
  @Input() count!: number;

  @Output() getMockData = new EventEmitter();
  @Output() generateMockDataEvent = new EventEmitter<number>();
  @Output() editResourceEvent = new EventEmitter();
  @Output() deleteResourceEvent = new EventEmitter();

  onCountChange(count: number | undefined): void {
    count ? this.generateMockDataEvent.emit(count) : null;
  }


}
