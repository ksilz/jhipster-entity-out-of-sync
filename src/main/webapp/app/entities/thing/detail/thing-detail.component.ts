import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IThing } from '../thing.model';

@Component({
  selector: 'jhi-thing-detail',
  templateUrl: './thing-detail.component.html',
})
export class ThingDetailComponent implements OnInit {
  thing: IThing | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ thing }) => {
      this.thing = thing;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
