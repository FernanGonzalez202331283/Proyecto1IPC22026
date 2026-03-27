import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-operaciones',
  standalone: true,
  imports: [],
  templateUrl: './operaciones.html',
  styleUrl: './operaciones.css',
})
export class Operaciones {
  constructor(private router: Router) {}

  irDestinos(accion: string) {
    this.router.navigate(['/destinos'], { queryParams: { accion } });
  }

  irPaquetes(accion: string) {
    this.router.navigate(['/paquetes'], { queryParams: { accion } });
  }

  irServicio(accion: string) {
    this.router.navigate(['/servicio'], { queryParams: { accion } });
  }

  irProveedores(accion: string) {
    this.router.navigate(['/proveedores'], { queryParams: { accion } });
  }

  regresar(){
    localStorage.clear();
    this.router.navigate(['']);
  }
}
