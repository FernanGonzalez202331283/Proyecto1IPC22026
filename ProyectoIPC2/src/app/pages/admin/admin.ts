import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {

  constructor(private router: Router) {}
 irAtencion() {
    this.router.navigate(['/atencion']);
  }

  irOperaciones() {
    this.router.navigate(['/operaciones']);
  }

  irReporte(tipo: string) {
    this.router.navigate(['/reporte', tipo]);
  }

  irUsuarios(){
    this.router.navigate(['/usuario']);
  }
  
  irCargaDatos(){
    this.router.navigate(['/carga-datos']);
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['']);
  }
}
