import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reembolsos',
  imports: [FormsModule, CommonModule],
  templateUrl: './reembolsos.html',
  styleUrl: './reembolsos.css',
})
export class Reembolsos {
  router = inject(Router);
reservacionId: number = 0;
  reservacion: any = null;
  mensaje: string = '';
  reembolso: number | null = null;

  cargarReservacion() {
    if (!this.reservacionId) {
      this.mensaje = 'Ingrese un ID válido';
      return;
    }

    fetch(`http://localhost:8080/Proyecto1IPC2/DetalleReservacionServlet?id=${this.reservacionId}`, {
      method: 'GET',
      credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {
      if (data.error) {
        this.mensaje = data.error;
        this.reservacion = null;
      } else {
        this.reservacion = data;
        this.mensaje = '';
      }
    })
    .catch(err => console.error(err));
  }

  cancelarReservacion() {
    if (!this.reservacion) return;

    fetch('http://localhost:8080/Proyecto1IPC2/CancelacionServlet', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: `reservacion_id=${this.reservacion.id}`
    })
    .then(res => res.json())
    .then(data => {
      if (data.error) {
        this.mensaje = data.error;
      } else if (data.status === 'ok') {
        this.reembolso = data.reembolso;
this.mensaje = 'Reservación cancelada correctamente';
this.reservacion.estado = 'CANCELADA';
      }
    })
    .catch(err => console.error(err));
  }
  regresar() {
    this.router.navigate(['/atencion']);
  }
}
