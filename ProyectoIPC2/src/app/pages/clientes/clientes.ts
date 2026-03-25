import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
})
export class Clientes {

  accion: string = '';
  mostrarBotonRegresar: boolean = false;

  cliente = {
    dpi: '',
    nombre: '',
    fecha: '',
    telefono: '',
    correo: '',
    nacionalidad: ''
  };

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.accion = params['accion'];
    });
  }

  guardarCliente() {
    const url = 'http://localhost:8080/Proyecto1IPC2/ClienteServlet';

    const body = new HttpParams()
      .set('dpi', this.cliente.dpi)
      .set('nombre', this.cliente.nombre)
      .set('fecha', this.cliente.fecha)
      .set('telefono', this.cliente.telefono)
      .set('correo', this.cliente.correo)
      .set('nacionalidad', this.cliente.nacionalidad);

    this.http.post(url, body.toString(), {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      withCredentials: true
    }).subscribe({
      next: (res: any) => {
        console.log('Respuesta backend:', res);

        if (res.status === 'ok') {
          alert(res.mensaje || 'Cliente creado');

          this.cliente = {
            dpi: '',
            nombre: '',
            fecha: '',
            telefono: '',
            correo: '',
            nacionalidad: ''
          };

          this.mostrarBotonRegresar = true;
        } else {
          alert(res.error || res.mensaje || 'No se pudo guardar');
        }
      },
      error: (err) => {
        console.error('Error al guardar cliente:', err);
        alert('Error al guardar');
      }
    });
  }
    actualizarCliente() {
  const url = 'http://localhost:8080/Proyecto1IPC2/ClienteServlet';

  const body = new HttpParams()
    .set('accion', 'actualizar')
    .set('dpi', this.cliente.dpi)
    .set('nombre', this.cliente.nombre)
    .set('fecha', this.cliente.fecha)
    .set('telefono', this.cliente.telefono)
    .set('correo', this.cliente.correo)
    .set('nacionalidad', this.cliente.nacionalidad);

  this.http.post(url, body.toString(), {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    withCredentials: true
  }).subscribe({
    next: (res: any) => {
      if (res.status === 'ok') {
        alert(res.mensaje || 'Cliente actualizado');
        this.limpiarCampos();
        this.mostrarBotonRegresar = true;
      } else {
        alert(res.error || res.mensaje || 'No se pudo actualizar');
      }
    },
    error: (err) => {
      console.error(err);
      alert('Error al actualizar cliente');
    }
  });
}

  limpiarCampos() {
    this.cliente = {
      dpi: '',
      nombre: '',
      fecha: '',
      telefono: '',
      correo: '',
      nacionalidad: ''
    };
}
buscarCliente() {
  const url = 'http://localhost:8080/Proyecto1IPC2/ClienteServlet';
  const params = new HttpParams().set('dpi', this.cliente.dpi);

  this.http.get(url, { params, withCredentials: true }).subscribe({
    next: (res: any) => {
      console.log('Respuesta búsqueda:', res);

      if (res.dpi) {
        this.cliente.dpi = res.dpi;
        this.cliente.nombre = res.nombre;
        this.cliente.fecha = res.fecha;
        this.cliente.telefono = res.telefono;
        this.cliente.correo = res.correo;
        this.cliente.nacionalidad = res.nacionalidad;
      } else {
        alert(res.mensaje || 'Cliente no encontrado');
      }
    },
    error: (err) => {
      console.error('Error al buscar cliente:', err);
      alert('Error al buscar cliente');
    }
  });
}
  regresar() {
    this.router.navigate(['/atencion']);
  }
}
