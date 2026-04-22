import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Allievo, CreateAllievoRequest, UpdateAllievoRequest } from '@shared/models';

/**
 * Servizio di accesso ai dati degli allievi.
 * Centralizza tutte le chiamate HTTP verso il backend per ricerca, dettaglio e operazioni CRUD.
 */
@Injectable({ providedIn: 'root' })
export class AllievoService {
  private readonly http = inject(HttpClient);
  /** Base URL dell'API allievi esposta dal backend. */
  private readonly apiBase = 'http://localhost:8080/api/allievi';
  private readonly importBase = 'http://localhost:8080/api/import';

  /** Recupera l'elenco completo degli allievi. */
  findAll(): Observable<Allievo[]> {
    return this.http.get<Allievo[]>(this.apiBase);
  }

  /**
   * Esegue la ricerca trasversale per nome, cognome o codice fiscale.
   * Il backend applica il filtro testuale sulla query custom GET /api/allievi.
   */
  search(search: string): Observable<Allievo[]> {
    const params = new HttpParams().set('search', search);
    return this.http.get<Allievo[]>(this.apiBase, { params });
  }

  /** Recupera gli allievi associati a uno specifico corso. */
  findByCorso(corsoId: number): Observable<Allievo[]> {
    return this.http.get<Allievo[]>(`${this.apiBase}?corsoId=${corsoId}`);
  }

  /** Recupera il dettaglio di un singolo allievo tramite id. */
  findById(id: number): Observable<Allievo> {
    return this.http.get<Allievo>(`${this.apiBase}/${id}`);
  }

  /** Crea un nuovo allievo passando il payload richiesto dal backend. */
  create(payload: CreateAllievoRequest): Observable<Allievo> {
    return this.http.post<Allievo>(this.apiBase, payload);
  }

  /** Aggiorna un allievo esistente con il payload normalizzato dalla UI. */
  update(id: number, payload: UpdateAllievoRequest): Observable<Allievo> {
    return this.http.put<Allievo>(`${this.apiBase}/${id}`, payload);
  }

  /** Elimina un allievo in modo definitivo. */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBase}/${id}`);
  }

  /** Importa allievi da un file Excel .xlsx. */
  importAllievi(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.importBase}/allievi`, formData, {
      responseType: 'text',
    });
  }
}