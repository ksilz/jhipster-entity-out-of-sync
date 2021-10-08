jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ThingService } from '../service/thing.service';
import { IThing, Thing } from '../thing.model';

import { ThingUpdateComponent } from './thing-update.component';

describe('Component Tests', () => {
  describe('Thing Management Update Component', () => {
    let comp: ThingUpdateComponent;
    let fixture: ComponentFixture<ThingUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let thingService: ThingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ThingUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ThingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ThingUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      thingService = TestBed.inject(ThingService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const thing: IThing = { id: 456 };

        activatedRoute.data = of({ thing });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(thing));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Thing>>();
        const thing = { id: 123 };
        jest.spyOn(thingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ thing });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: thing }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(thingService.update).toHaveBeenCalledWith(thing);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Thing>>();
        const thing = new Thing();
        jest.spyOn(thingService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ thing });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: thing }));
        saveSubject.complete();

        // THEN
        expect(thingService.create).toHaveBeenCalledWith(thing);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Thing>>();
        const thing = { id: 123 };
        jest.spyOn(thingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ thing });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(thingService.update).toHaveBeenCalledWith(thing);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
