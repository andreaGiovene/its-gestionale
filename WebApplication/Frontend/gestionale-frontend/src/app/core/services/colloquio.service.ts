import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Colloquio, ColloquiFilters, CreateColloquioRequest } from '@shared/models';

@Injectable({ providedIn: 'root' })
export class ColloquioService {
  private readonly http = inject(HttpClient);
  private readonly apiBase = 'http://localhost:8080/api/colloqui';

  findAll(filters?: ColloquiFilters): Observable<Colloquio[]> {
    let params = new HttpParams();

    if (filters?.allievoId !== undefined && filters.allievoId !== null) {
      params = params.set('allievoId', String(filters.allievoId));
    }

    if (filters?.aziendaId !== undefined && filters.aziendaId !== null) {
      params = params.set('aziendaId', String(filters.aziendaId));
    }

    return this.http.get<Colloquio[]>(this.apiBase, { params });
  }

  create(allievoId: number, aziendaId: number, payload: CreateColloquioRequest): Observable<Colloquio> {
    const params = new HttpParams()
      .set('allievoId', String(allievoId))
      .set('aziendaId', String(aziendaId));

    return this.http.post<Colloquio>(this.apiBase, payload, { params });
  }
}
