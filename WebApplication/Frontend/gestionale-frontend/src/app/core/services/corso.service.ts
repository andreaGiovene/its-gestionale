import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Corso, CreateCorsoRequest, UpdateCorsoRequest } from '@shared/models';

@Injectable({ providedIn: 'root' })
export class CorsoService {
  private readonly http = inject(HttpClient);
  private readonly apiBase = 'http://localhost:8080/api/corsi';

  /**
   * Recupera tutti i corsi
   */
  findAll(): Observable<Corso[]> {
    return this.http.get<Corso[]>(this.apiBase);
  }

  /**
   * Recupera un corso per ID
   */
  findById(id: number): Observable<Corso> {
    return this.http.get<Corso>(`${this.apiBase}/${id}`);
  }

  /**
   * Crea un nuovo corso
   */
  create(payload: CreateCorsoRequest): Observable<Corso> {
    return this.http.post<Corso>(this.apiBase, payload);
  }

  /**
   * Aggiorna un corso esistente
   */
  update(id: number, payload: UpdateCorsoRequest): Observable<Corso> {
    return this.http.put<Corso>(`${this.apiBase}/${id}`, payload);
  }

  /**
   * Elimina un corso
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBase}/${id}`);
  }
}
