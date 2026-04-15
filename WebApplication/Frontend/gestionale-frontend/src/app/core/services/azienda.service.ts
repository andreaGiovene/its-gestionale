import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Azienda, AziendaSearchFilters, CreateAziendaRequest, PageResponse, UpdateAziendaRequest, Contatto, CreateContattoRequest } from '@shared/models';
import { HttpParams } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AziendaService {
  private readonly http = inject(HttpClient);
  private readonly apiBase = 'http://localhost:8080/api/aziende';

  /**
   * Recupera aziende filtrate con paginazione e ordinamento server-side.
   */
  getCerca(filtri: AziendaSearchFilters): Observable<PageResponse<Azienda>> {
    let params = new HttpParams();

    if (filtri.tipo) {
      params = params.set('tipo', filtri.tipo);
    }

    if (filtri.ragioneSociale?.trim()) {
      params = params.set('ragioneSociale', filtri.ragioneSociale.trim());
    }

    if (filtri.corsoId !== null && filtri.corsoId !== undefined) {
      params = params.set('corsoId', String(filtri.corsoId));
    }

    params = params.set('page', String(filtri.page ?? 0));
    params = params.set('size', String(filtri.size ?? 10));

    // Se fornito un sortDirection, lo manda al backend via sort parameter (formato Spring Data: field,direction)
    if (filtri.sortDirection) {
      params = params.set('sort', `ragioneSociale,${filtri.sortDirection}`);
    }

    return this.http.get<PageResponse<Azienda>>(this.apiBase, { params });
  }

  /**
   * Recupera tutte le aziende.
   */
  findAll(): Observable<Azienda[]> {
    return this.getCerca({ page: 0, size: 1000 }).pipe(map((response) => response.content));
  }

  /**
   * Recupera un'azienda per ID
   */
  findById(id: number): Observable<Azienda> {
    return this.http.get<Azienda>(`${this.apiBase}/${id}`);
  }

  /**
   * Crea una nuova azienda
   */
  create(payload: CreateAziendaRequest): Observable<Azienda> {
    return this.http.post<Azienda>(this.apiBase, payload);
  }

  /**
   * Aggiorna un'azienda esistente
   */
  update(id: number, payload: UpdateAziendaRequest): Observable<Azienda> {
    return this.http.put<Azienda>(`${this.apiBase}/${id}`, payload);
  }

  /**
   * Elimina un'azienda
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBase}/${id}`);
  }

  /**
   * Recupera i contatti aziendali di un'azienda
   */
  getContatti(aziendaId: number): Observable<Contatto[]> {
    return this.http.get<Contatto[]>(`${this.apiBase}/${aziendaId}/contatti`);
  }

  /**
   * Inserisce un nuovo contatto aziendale.
   */
  createContatto(aziendaId: number, payload: CreateContattoRequest): Observable<Contatto> {
    return this.http.post<Contatto>(`${this.apiBase}/${aziendaId}/contatti`, payload);
  }
}
