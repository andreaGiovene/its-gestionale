export type StatoEsitoColloquio =
  | 'IN_ATTESA'
  | 'POSITIVO'
  | 'NEGATIVO'
  | 'RITIRATO'
  | 'NON_PRESENTATO';

export type TipoEventoColloquio = 'MATCHING_DAY' | 'FUORI_MATCHING_DAY';

export interface Colloquio {
  id: number;
  dataColloquio: string;
  tipoEvento?: TipoEventoColloquio;
  esito: StatoEsitoColloquio;
  noteFeedback?: string;
  allievoId?: number;
  allievoNome?: string;
  allievano?: string;
  aziendaId?: number;
  aziendaRagioneSociale?: string;
}

export interface CreateColloquioRequest {
  dataColloquio: string;
  tipoEvento?: TipoEventoColloquio;
  esito?: StatoEsitoColloquio;
  noteFeedback?: string;
}

export interface ColloquiFilters {
  allievoId?: number;
  aziendaId?: number;
}
