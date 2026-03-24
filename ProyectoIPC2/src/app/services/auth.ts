import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private url = 'http://localhost:8080/Proyecto1IPC2/LoginServlet';
   
  constructor(private http: HttpClient) {}
  login(username: string, password: string): Observable<any> {

    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    return this.http.post(this.url, body.toString(), {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });
  }
}