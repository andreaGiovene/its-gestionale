export interface Azienda {
  id: number;
  ragioneSociale: string;
  partitaIva: string;
  telefono?: string;
  email?: string;
  indirizzo?: string;
  cap?: string;
  citta?: string;
}

export type AziendaSearchTipo = 'MADRINA' | 'NON_MADRINA';

export interface AziendaSearchFilters {
  tipo?: AziendaSearchTipo | '';
  ragioneSociale?: string;
  corsoId?: number | null;
  page?: number;
  size?: number;
  sortDirection?: 'asc' | 'desc';
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first?: boolean;
  last?: boolean;
}

export interface CreateAziendaRequest {
  ragioneSociale: string;
  partitaIva: string;
  telefono?: string;
  email?: string;
  indirizzo?: string;
  cap?: string;
  citta?: string;
}

export interface UpdateAziendaRequest extends CreateAziendaRequest {}
