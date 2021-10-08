import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IThing, getThingIdentifier } from '../thing.model';

export type EntityResponseType = HttpResponse<IThing>;
export type EntityArrayResponseType = HttpResponse<IThing[]>;

@Injectable({ providedIn: 'root' })
export class ThingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/things');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(thing: IThing): Observable<EntityResponseType> {
    return this.http.post<IThing>(this.resourceUrl, thing, { observe: 'response' });
  }

  update(thing: IThing): Observable<EntityResponseType> {
    return this.http.put<IThing>(`${this.resourceUrl}/${getThingIdentifier(thing) as number}`, thing, { observe: 'response' });
  }

  partialUpdate(thing: IThing): Observable<EntityResponseType> {
    return this.http.patch<IThing>(`${this.resourceUrl}/${getThingIdentifier(thing) as number}`, thing, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IThing>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IThing[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addThingToCollectionIfMissing(thingCollection: IThing[], ...thingsToCheck: (IThing | null | undefined)[]): IThing[] {
    const things: IThing[] = thingsToCheck.filter(isPresent);
    if (things.length > 0) {
      const thingCollectionIdentifiers = thingCollection.map(thingItem => getThingIdentifier(thingItem)!);
      const thingsToAdd = things.filter(thingItem => {
        const thingIdentifier = getThingIdentifier(thingItem);
        if (thingIdentifier == null || thingCollectionIdentifiers.includes(thingIdentifier)) {
          return false;
        }
        thingCollectionIdentifiers.push(thingIdentifier);
        return true;
      });
      return [...thingsToAdd, ...thingCollection];
    }
    return thingCollection;
  }
}
