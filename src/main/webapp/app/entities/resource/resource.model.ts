import { IMock } from 'app/entities/mock/mock.model';

export interface IResource {
  id: string;
  name?: string | null;
  generator?: string | null;
  count?: number | null;
  mock?: Pick<IMock, 'id'> | null;
}

export type NewResource = Omit<IResource, 'id'> & { id: null };
