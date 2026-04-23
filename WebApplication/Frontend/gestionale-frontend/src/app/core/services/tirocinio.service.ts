import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateTirocinioRequest, TirociniFilters, Tirocinio } from '@shared/models';

@Injectable({ providedIn: 'root' })
export class TirocinioService {
  private readonly http = inject(HttpClient);
  private readonly apiBase = 'http://localhost:8080/api/tirocini';

  findAll(filters?: TirociniFilters): Observable<Tirocinio[]> {
    let params = new HttpParams();

    if (filters?.allievoId !== undefined && filters.allievoId !== null) {
      params = params.set('allievoId', String(filters.allievoId));
    }

    if (filters?.aziendaId !== undefined && filters.aziendaId !== null) {
      params = params.set('aziendaId', String(filters.aziendaId));
    }

    if (filters?.start) {
      params = params.set('start', filters.start);
    }

    if (filters?.end) {
      params = params.set('end', filters.end);
    }

    return this.http.get<Tirocinio[]>(this.apiBase, { params });
  }

  create(allievoId: number, aziendaId: number, payload: CreateTirocinioRequest): Observable<Tirocinio> {
    const params = new HttpParams()
      .set('allievoId', String(allievoId))
      .set('aziendaId', String(aziendaId));

    return this.http.post<Tirocinio>(this.apiBase, payload, { params });
  }

  update(id: number, payload: CreateTirocinioRequest): Observable<Tirocinio> {
    return this.http.put<Tirocinio>(`${this.apiBase}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBase}/${id}`);
  }
}
