import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})

export class ReporteService{

  private url = 'http://localhost:8080/Proyecto1IPC2/ReporteServlet';

  constructor(private http: HttpClient) {}

  obtener(tipo: string, inicio: string, fin: string) {
    const params = new HttpParams()
      .set('tipo', tipo)
      .set('inicio', inicio)
      .set('fin', fin);

    return this.http.get<any[]>(this.url, { params, withCredentials: true });
  }
}
