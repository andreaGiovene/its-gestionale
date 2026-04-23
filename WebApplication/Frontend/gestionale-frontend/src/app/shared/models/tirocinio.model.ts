export type StatoTirocinio = 'IN_CORSO' | 'CONCLUSO' | 'INTERROTTO';

export interface Tirocinio {
  id: number;
  dataInizio: string;
  dataFine?: string;
  tipo?: string;
  frequenza?: string;
  esito: StatoTirocinio;
  allievoId?: number;
  aziendaId?: number;
}

export interface CreateTirocinioRequest {
  dataInizio: string;
  dataFine?: string;
  tipo?: string;
  frequenza?: string;
  esito: StatoTirocinio;
}

export interface TirociniFilters {
  allievoId?: number;
  aziendaId?: number;
  start?: string;
  end?: string;
}
