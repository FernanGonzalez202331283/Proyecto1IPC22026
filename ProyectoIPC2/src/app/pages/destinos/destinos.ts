import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-destinos',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './destinos.html',
  styleUrl: './destinos.css',
})

export class Destinos {

  constructor(private router: Router, private route: ActivatedRoute) {}
  accion: string = 'menu';
  // CONTROL DE VISTA
  id: string = '';
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

  guardarDestino() {
  if (!this.nombre || !this.pais || !this.imagen_url) {
    this.mensaje = 'Complete los campos obligatorios';
    return;
  }

  fetch('http://localhost:8080/Proyecto1IPC2/DestinoServlet', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `accion=crear&nombre=${encodeURIComponent(this.nombre)}&pais=${encodeURIComponent(this.pais)}&descripcion=${encodeURIComponent(this.descripcion)}&clima=${encodeURIComponent(this.clima)}&imagen_url=${encodeURIComponent(this.imagen_url)}`
  })
  .then(res => res.text()) // 👈 CAMBIO IMPORTANTE
  .then(res => {
    console.log("RESPUESTA RAW:", res);

    try {
      const data = JSON.parse(res);

      if (data.error) {
        this.mensaje = data.error;
      } else {
        this.mensaje = data.mensaje || 'Destino creado correctamente ✅';
      }

    } catch (e) {
      // por si el backend manda texto plano
      this.mensaje = res;
    }

    // LIMPIAR FORM
    this.nombre = '';
    this.pais = '';
    this.descripcion = '';
    this.clima = '';
    this.imagen_url = '';

    // OCULTAR MENSAJE
    setTimeout(() => {
      this.mensaje = '';
    }, 3000);
  })
  .catch(err => {
    console.error(err);
    this.mensaje = 'Error de conexión';
  });
}

  editarDestino() {

  if (!this.id) {
    this.mensaje = 'ID requerido';
    return;
  }

  fetch('http://localhost:8080/Proyecto1IPC2/DestinoServlet', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `accion=editar&id=${this.id}&nombre=${this.nombre}&pais=${this.pais}&descripcion=${this.descripcion}&clima=${this.clima}&imagen_url=${this.imagen_url}`
  })
  .then(res => res.json())
  .then(data => {
    this.mensaje = data?.mensaje || data?.error;
  });
}


eliminarDestino() {

  if (!this.id) {
    this.mensaje = 'ID requerido';
    return;
  }

  fetch('http://localhost:8080/Proyecto1IPC2/DestinoServlet', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `accion=eliminar&id=${this.id}`
  })
  .then(res => res.json())
  .then(data => {
    this.mensaje = data?.mensaje || data?.error;
  });
}
   regresar() {
    this.router.navigate(['/operaciones']);
  }
  ngOnInit() {
  this.route.queryParams.subscribe(params => {
    this.accion = params['accion'] || 'menu';
  });
}
}
