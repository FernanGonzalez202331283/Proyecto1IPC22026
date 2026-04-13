import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {

 private url = 'http://localhost:8080/Proyecto1IPC2/UsuarioServlet';

  constructor(private http: HttpClient) {}

  listar(): Observable<any> {
  return this.http.get(this.url, { withCredentials: true });
}

 crear(usuario: any): Observable<any> {
  const body = new HttpParams()
    .set('accion', 'crear') 
    .set('username', usuario.username)
    .set('password', usuario.password)
    .set('rol', usuario.rol);

  return this.http.post(this.url, body, { withCredentials: true });
}

cambiarRol(id: number, rol: string) {
  const body = new HttpParams()
    .set('accion', 'cambiarRol') 
    .set('id', id)
    .set('rol', rol);

  return this.http.post(this.url, body, { withCredentials: true });
}

cambiarEstado(id: number, estado: boolean) {
  const body = new HttpParams()
    .set('accion', estado ? 'activar' : 'desactivar') 
    .set('id', id);

  return this.http.post(this.url, body, { withCredentials: true });
}
}
