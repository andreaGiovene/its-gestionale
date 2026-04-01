export interface Azienda {
  id: number;
  ragioneSociale: string;
  partitaIva: string;
  telefono?: string;
  email?: string;
  indirizzo?: string;
  cap?: string;
}

export interface CreateAziendaRequest {
  ragioneSociale: string;
  partitaIva: string;
  telefono?: string;
  email?: string;
  indirizzo?: string;
  cap?: string;
}

export interface UpdateAziendaRequest extends CreateAziendaRequest {}
