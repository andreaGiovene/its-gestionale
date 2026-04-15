export interface Contatto {
  id: number;
  nome: string;
  cognome: string;
  ruolo: 'TITOLARE' | 'DIRETTORE' | 'RESPONSABILE_HR' | 'RESPONSABILE_TIROCINI' | 'TUTOR_AZIENDALE';
  telefono?: string;
  email?: string;
}

export interface CreateContattoRequest {
  nome: string;
  cognome: string;
  ruolo: 'TITOLARE' | 'DIRETTORE' | 'RESPONSABILE_HR' | 'RESPONSABILE_TIROCINI' | 'TUTOR_AZIENDALE';
  telefono?: string;
  email?: string;
}

export interface UpdateContattoRequest extends CreateContattoRequest {}
