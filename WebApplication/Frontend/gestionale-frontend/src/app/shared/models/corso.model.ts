export interface Corso {
  id: number;
  nome: string;
  annoAccademico: string;
  stato: string;
  aziendaMadrinaId?: number;
  aziendaMadrinaRagioneSociale?: string;
  allieviCount?: number;
}

export interface CreateCorsoRequest {
  nome: string;
  annoAccademico: string;
  stato: string;
}

export interface UpdateCorsoRequest extends CreateCorsoRequest {}
