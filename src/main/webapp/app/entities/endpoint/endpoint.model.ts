import { IResource } from 'app/entities/resource/resource.model';

export interface IEndpoint {
  id: string;
  url?: string | null;
  method?: string | null;
  enabled?: boolean | null;
  response?: string | null;
  resource?: Pick<IResource, 'id'> | null;
}

export type NewEndpoint = Omit<IEndpoint, 'id'> & { id: null };
