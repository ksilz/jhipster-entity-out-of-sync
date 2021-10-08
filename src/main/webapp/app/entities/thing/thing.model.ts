export interface IThing {
  id?: number;
  enableSomething?: boolean;
}

export class Thing implements IThing {
  constructor(public id?: number, public enableSomething?: boolean) {
    this.enableSomething = this.enableSomething ?? false;
  }
}

export function getThingIdentifier(thing: IThing): number | undefined {
  return thing.id;
}
