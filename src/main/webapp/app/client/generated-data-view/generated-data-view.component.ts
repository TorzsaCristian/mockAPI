import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'jhi-generated-data-view',
  templateUrl: './generated-data-view.component.html',
  styleUrls: ['./generated-data-view.component.scss']
})
export class GeneratedDataViewComponent {

  @Input() data: string | null = null;

  @Output() closeDialogEvent = new EventEmitter();
  @Output() updateMockDataEvent = new EventEmitter<string>();

  submitUpdate(): void {
    if (this.isJsonString(this.data!)) {
      this.updateMockDataEvent.emit(this.data!);
    }
  }

  isJsonString(str: string): boolean {
    try {
      JSON.parse(str);
    } catch (e) {
      return false;
    }
    return true;
  }

}
