import { IMock } from 'app/entities/mock/mock.model';
import { IProject } from 'app/entities/project/project.model';

export interface IResource {
  id: string;
  name?: string | null;
  generator?: string | null;
  count?: number | null;
  mock?: Pick<IMock, 'id'> | null;
  project?: Pick<IProject, 'id'> | null;
}

export type NewResource = Omit<IResource, 'id'> & { id: null };
