import { IResource } from 'app/entities/resource/resource.model';

export interface IResourceSchema {
  id: string;
  name?: string | null;
  type?: string | null;
  fakerMethod?: string | null;
  resource?: Pick<IResource, 'id'> | null;
}

export type NewResourceSchema = Omit<IResourceSchema, 'id'> & { id: null };
