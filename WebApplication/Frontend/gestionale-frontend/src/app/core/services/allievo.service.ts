import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Allievo, CreateAllievoRequest, UpdateAllievoRequest } from '@shared/models';

@Injectable({ providedIn: 'root' })
export class AllievoService {
  private readonly http = inject(HttpClient);
  private readonly apiBase = 'http://localhost:8080/api/allievi';

  findAll(): Observable<Allievo[]> {
    return this.http.get<Allievo[]>(this.apiBase);
  }

  findByCorso(corsoId: number): Observable<Allievo[]> {
    return this.http.get<Allievo[]>(`${this.apiBase}?corsoId=${corsoId}`);
  }

  findById(id: number): Observable<Allievo> {
    return this.http.get<Allievo>(`${this.apiBase}/${id}`);
  }

  create(payload: CreateAllievoRequest): Observable<Allievo> {
    return this.http.post<Allievo>(this.apiBase, payload);
  }

  update(id: number, payload: UpdateAllievoRequest): Observable<Allievo> {
    return this.http.put<Allievo>(`${this.apiBase}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBase}/${id}`);
  }
}