import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IThing, Thing } from '../thing.model';
import { ThingService } from '../service/thing.service';

@Injectable({ providedIn: 'root' })
export class ThingRoutingResolveService implements Resolve<IThing> {
  constructor(protected service: ThingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IThing> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((thing: HttpResponse<Thing>) => {
          if (thing.body) {
            return of(thing.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Thing());
  }
}
