export interface Allievo {
  id: number;
  nome: string;
  cognome: string;
  codiceFiscale?: string;
  email?: string;
  telefono?: string;
  dataDiNascita?: string;
  note?: string;
  corsoId?: number;
  corsoNome?: string;
}

export interface CreateAllievoRequest {
  nome: string;
  cognome: string;
  codiceFiscale?: string;
  email?: string;
  telefono?: string;
  dataDiNascita?: string;
  note?: string;
  corsoId?: number | null;
}

export interface UpdateAllievoRequest extends CreateAllievoRequest {}