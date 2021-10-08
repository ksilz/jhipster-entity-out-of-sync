jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IThing, Thing } from '../thing.model';
import { ThingService } from '../service/thing.service';

import { ThingRoutingResolveService } from './thing-routing-resolve.service';

describe('Service Tests', () => {
  describe('Thing routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ThingRoutingResolveService;
    let service: ThingService;
    let resultThing: IThing | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ThingRoutingResolveService);
      service = TestBed.inject(ThingService);
      resultThing = undefined;
    });

    describe('resolve', () => {
      it('should return IThing returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultThing = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultThing).toEqual({ id: 123 });
      });

      it('should return new IThing if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultThing = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultThing).toEqual(new Thing());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Thing })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultThing = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultThing).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
