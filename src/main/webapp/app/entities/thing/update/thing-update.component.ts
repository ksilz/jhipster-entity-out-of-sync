import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IThing, Thing } from '../thing.model';
import { ThingService } from '../service/thing.service';

@Component({
  selector: 'jhi-thing-update',
  templateUrl: './thing-update.component.html',
})
export class ThingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    enableSomething: [null, [Validators.required]],
  });

  constructor(protected thingService: ThingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ thing }) => {
      this.updateForm(thing);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const thing = this.createFromForm();
    if (thing.id !== undefined) {
      this.subscribeToSaveResponse(this.thingService.update(thing));
    } else {
      this.subscribeToSaveResponse(this.thingService.create(thing));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IThing>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(thing: IThing): void {
    this.editForm.patchValue({
      id: thing.id,
      enableSomething: thing.enableSomething,
    });
  }

  protected createFromForm(): IThing {
    return {
      ...new Thing(),
      id: this.editForm.get(['id'])!.value,
      enableSomething: this.editForm.get(['enableSomething'])!.value,
    };
  }
}
