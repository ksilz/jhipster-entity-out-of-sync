import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ThingComponent } from '../list/thing.component';
import { ThingDetailComponent } from '../detail/thing-detail.component';
import { ThingUpdateComponent } from '../update/thing-update.component';
import { ThingRoutingResolveService } from './thing-routing-resolve.service';

const thingRoute: Routes = [
  {
    path: '',
    component: ThingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ThingDetailComponent,
    resolve: {
      thing: ThingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ThingUpdateComponent,
    resolve: {
      thing: ThingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ThingUpdateComponent,
    resolve: {
      thing: ThingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(thingRoute)],
  exports: [RouterModule],
})
export class ThingRoutingModule {}
