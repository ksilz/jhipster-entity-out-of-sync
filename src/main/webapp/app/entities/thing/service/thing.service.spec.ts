import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IThing, Thing } from '../thing.model';

import { ThingService } from './thing.service';

describe('Service Tests', () => {
  describe('Thing Service', () => {
    let service: ThingService;
    let httpMock: HttpTestingController;
    let elemDefault: IThing;
    let expectedResult: IThing | IThing[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ThingService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        enableSomething: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Thing', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Thing()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Thing', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            enableSomething: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Thing', () => {
        const patchObject = Object.assign({}, new Thing());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Thing', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            enableSomething: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Thing', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addThingToCollectionIfMissing', () => {
        it('should add a Thing to an empty array', () => {
          const thing: IThing = { id: 123 };
          expectedResult = service.addThingToCollectionIfMissing([], thing);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(thing);
        });

        it('should not add a Thing to an array that contains it', () => {
          const thing: IThing = { id: 123 };
          const thingCollection: IThing[] = [
            {
              ...thing,
            },
            { id: 456 },
          ];
          expectedResult = service.addThingToCollectionIfMissing(thingCollection, thing);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Thing to an array that doesn't contain it", () => {
          const thing: IThing = { id: 123 };
          const thingCollection: IThing[] = [{ id: 456 }];
          expectedResult = service.addThingToCollectionIfMissing(thingCollection, thing);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(thing);
        });

        it('should add only unique Thing to an array', () => {
          const thingArray: IThing[] = [{ id: 123 }, { id: 456 }, { id: 72998 }];
          const thingCollection: IThing[] = [{ id: 123 }];
          expectedResult = service.addThingToCollectionIfMissing(thingCollection, ...thingArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const thing: IThing = { id: 123 };
          const thing2: IThing = { id: 456 };
          expectedResult = service.addThingToCollectionIfMissing([], thing, thing2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(thing);
          expect(expectedResult).toContain(thing2);
        });

        it('should accept null and undefined values', () => {
          const thing: IThing = { id: 123 };
          expectedResult = service.addThingToCollectionIfMissing([], null, thing, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(thing);
        });

        it('should return initial array if no Thing is added', () => {
          const thingCollection: IThing[] = [{ id: 123 }];
          expectedResult = service.addThingToCollectionIfMissing(thingCollection, undefined, null);
          expect(expectedResult).toEqual(thingCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
