import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-destinos',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './destinos.html',
  styleUrl: './destinos.css',
})

export class Destinos {

  constructor(private router: Router) {}

  // CONTROL DE VISTA
  accion: string = '';

  // CAMPOS DEL FORM
  nombre: string = '';
  pais: string = '';
  descripcion: string = '';
  clima: string = '';
  imagen_url: string = '';

  mensaje: string = '';

  // CAMBIAR VISTA
  irDestinos(op: string) {
    this.accion = op;
  }

  // GUARDAR DESTINO
  guardarDestino() {
    if (!this.nombre || !this.pais || !this.imagen_url) {
      this.mensaje = 'Complete los campos obligatorios';
      return;
    }

    fetch('http://localhost:8080/Proyecto1IPC2/DestinoServlet', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: `nombre=${this.nombre}&pais=${this.pais}&descripcion=${this.descripcion}&clima=${this.clima}&imagen_url=${this.imagen_url}`
    })
    .then(res => res.json())
    .then(data => {
      if (data.error) {
        this.mensaje = data.error;
      } else {
        this.mensaje = 'Destino guardado correctamente ✅';

        // LIMPIAR FORM
        this.nombre = '';
        this.pais = '';
        this.descripcion = '';
        this.clima = '';
        this.imagen_url = '';
      }
    })
    .catch(err => console.error(err));
  }

   regresar() {
    this.router.navigate(['/operaciones']);
  }

}
