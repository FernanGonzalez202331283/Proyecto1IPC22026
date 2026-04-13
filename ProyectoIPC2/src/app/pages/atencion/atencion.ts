import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-atencion',
  standalone: true,
  imports: [],
  templateUrl: './atencion.html',
  styleUrl: './atencion.css',
})

export class Atencion {
  constructor(private router: Router) {}

   irClientes(accion: string) {
    this.router.navigate(['/clientes'], { queryParams: { accion } });
  }

  irReservaciones(accion: string) {
    this.router.navigate(['/reservaciones'], { queryParams: { accion } });
  }

  irPagos() {
    this.router.navigate(['/pagos']);
  }

  irReembolsos() {
    this.router.navigate(['/reembolsos']);
  }
 regresar(){
  const rol = localStorage.getItem('rol');

  if (rol === 'ADMIN') {
    this.router.navigate(['/admin']);
  } else {
    // otros roles regresan al login
    localStorage.clear();
    this.router.navigate(['']);
  }
}
}
