import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ThingComponent } from './list/thing.component';
import { ThingDetailComponent } from './detail/thing-detail.component';
import { ThingUpdateComponent } from './update/thing-update.component';
import { ThingDeleteDialogComponent } from './delete/thing-delete-dialog.component';
import { ThingRoutingModule } from './route/thing-routing.module';

@NgModule({
  imports: [SharedModule, ThingRoutingModule],
  declarations: [ThingComponent, ThingDetailComponent, ThingUpdateComponent, ThingDeleteDialogComponent],
  entryComponents: [ThingDeleteDialogComponent],
})
export class ThingModule {}
