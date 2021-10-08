import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IThing } from '../thing.model';
import { ThingService } from '../service/thing.service';

@Component({
  templateUrl: './thing-delete-dialog.component.html',
})
export class ThingDeleteDialogComponent {
  thing?: IThing;

  constructor(protected thingService: ThingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.thingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
