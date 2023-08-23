export interface IMock {
  id: string;
  prefix?: string | null;
  version?: number | null;
}

export type NewMock = Omit<IMock, 'id'> & { id: null };
