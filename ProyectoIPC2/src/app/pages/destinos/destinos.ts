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
  constructor(
    private router: Router,
    private route: ActivatedRoute,
  ) {}
  destinos: any[] = [];
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

  if (op === 'editar' || op === 'eliminar') {
    this.cargarDestinos();
  }
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
      body: `accion=crear&nombre=${encodeURIComponent(this.nombre)}&pais=${encodeURIComponent(this.pais)}&descripcion=${encodeURIComponent(this.descripcion)}&clima=${encodeURIComponent(this.clima)}&imagen_url=${encodeURIComponent(this.imagen_url)}`,
    })
      .then((res) => res.text())
      .then((res) => {
        console.log('RESPUESTA RAW:', res);

        try {
          const data = JSON.parse(res);

          if (data.error) {
            this.mensaje = data.error;
          } else {
            this.mensaje = data.mensaje || 'Destino creado correctamente ';
          }
        } catch (e) {
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
      .catch((err) => {
        console.error(err);
        this.mensaje = 'Error de conexión';
      });
  }
  cargarDestinos() {
  fetch('http://localhost:8080/Proyecto1IPC2/DestinoServlet', {
    method: 'GET',
    credentials: 'include'
  })
    .then(res => res.text()) //CAMBIAR A TEXT
    .then(data => {
      console.log("RESPUESTA BACKEND:", data);

      try {
        this.destinos = JSON.parse(data);
      } catch (e) {
        console.error("NO ES JSON:", data);
        this.mensaje = data;
      }
    })
    .catch(err => {
      console.error(err);
      this.mensaje = 'Error al cargar destinos';
    });
}
seleccionarDestino(event: any) {
  const idSeleccionado = event.target.value;

  const destino = this.destinos.find(d => d.id == idSeleccionado);

  if (destino) {
    this.id = destino.id;
    this.nombre = destino.nombre;
    this.pais = destino.pais;
    this.descripcion = destino.descripcion;
    this.clima = destino.clima;
    this.imagen_url = destino.imagen_url;
  }
}

 editarDestino() {
  if (!this.id) {
    this.mensaje = 'Debe seleccionar un destino';
    return;
  }

  fetch('http://localhost:8080/Proyecto1IPC2/DestinoServlet', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `accion=editar&id=${this.id}
    &nombre=${encodeURIComponent(this.nombre)}
    &pais=${encodeURIComponent(this.pais)}
    &descripcion=${encodeURIComponent(this.descripcion)}
    &clima=${encodeURIComponent(this.clima)}
    &imagen_url=${encodeURIComponent(this.imagen_url)}`,
  })
    .then((res) => res.json())
    .then((data) => {
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
      body: `accion=eliminar&id=${this.id}`,
    })
      .then((res) => res.json())
      .then((data) => {
        this.mensaje = data?.mensaje || data?.error;
      });
  }
  regresar() {
    this.router.navigate(['/operaciones']);
  }
  ngOnInit() {
  this.route.queryParams.subscribe((params) => {
    this.accion = params['accion'] || 'menu';
    if (this.accion === 'editar' || this.accion === 'eliminar') {
      this.cargarDestinos();
    }
  });
}
}
