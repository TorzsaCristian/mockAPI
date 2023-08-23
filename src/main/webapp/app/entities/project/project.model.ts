import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IProject {
  id: string;
  resourceTreeId?: string | null;
  name?: string | null;
  prefix?: string | null;
  isPublic?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  owner?: Pick<IUser, 'id'> | null;
  collaborators?: Pick<IUser, 'id'>[] | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
