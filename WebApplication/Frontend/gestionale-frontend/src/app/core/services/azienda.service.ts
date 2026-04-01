import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Azienda, CreateAziendaRequest, UpdateAziendaRequest } from '@shared/models';

@Injectable({ providedIn: 'root' })
export class AziendaService {
  private readonly http = inject(HttpClient);
  private readonly apiBase = 'http://localhost:8080/api/aziende';

  /**
   * Recupera tutte le aziende
   */
  findAll(): Observable<Azienda[]> {
    return this.http.get<Azienda[]>(this.apiBase);
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
}
