import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThingDetailComponent } from './thing-detail.component';

describe('Component Tests', () => {
  describe('Thing Management Detail Component', () => {
    let comp: ThingDetailComponent;
    let fixture: ComponentFixture<ThingDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ThingDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ thing: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ThingDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ThingDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load thing on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.thing).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
