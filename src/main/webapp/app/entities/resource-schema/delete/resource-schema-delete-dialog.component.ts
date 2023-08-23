import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IResourceSchema } from '../resource-schema.model';
import { ResourceSchemaService } from '../service/resource-schema.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './resource-schema-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResourceSchemaDeleteDialogComponent {
  resourceSchema?: IResourceSchema;

  constructor(protected resourceSchemaService: ResourceSchemaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.resourceSchemaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
