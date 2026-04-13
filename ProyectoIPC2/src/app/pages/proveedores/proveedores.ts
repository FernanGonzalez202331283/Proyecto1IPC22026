import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-proveedores',
  standalone: true,
  imports: [HttpClientModule, CommonModule, FormsModule],
  templateUrl: './proveedores.html',
  styleUrl: './proveedores.css',
})
export class Proveedores {
  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
  ) {}
  proveedores: any[] = [];
  proveedorSeleccionado: any = null;
  // control de la vista
  accion: string = 'crear';

  // campos del form
  nombre: string = '';
  tipo: string = '';
  pais: string = '';
  contacto: string = '';

  mensaje: string = '';

  // cambiar la visata

  irProveedores(op: string) {
    this.accion = op;
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.accion = params['accion'] || 'crear';
    });
    this.cargarProveedores();
  }

  //guardar al proveedor

  cargarProveedores() {
  this.http
    .get<any[]>('http://localhost:8080/Proyecto1IPC2/ProveedorServlet', { withCredentials: true })
    .subscribe({
      next: (res) => {
        this.proveedores = res;
      },
      error: (err) => console.error(err)
    });
}
seleccionarProveedor() {
  const p = this.proveedores.find(x => x.id == this.proveedorSeleccionado);

  if (p) {
    this.id = p.id;
    this.nombre = p.nombre;
    this.tipo = p.tipo;
    this.pais = p.pais;
    this.contacto = p.contacto;
  }
}
  enviarProveedor(accion: string) {
    if (accion === 'crear' || accion === 'editar') {
      if (!this.nombre || !this.tipo || !this.pais || !this.contacto) {
        this.mensaje = 'Todos los campos son obligatorios';
        return;
      }
    }

    if (accion !== 'crear' && (!this.id || this.id.toString().trim() === '')) {
      this.mensaje = 'ID requerido';
      return;
    }

    const data = {
      id: Number(this.id),
      nombre: this.nombre,
      tipo: this.tipo,
      pais: this.pais,
      contacto: this.contacto,
      accion: accion,
    };

    this.http
      .post('http://localhost:8080/Proyecto1IPC2/ProveedorServlet', data, { withCredentials: true })
      .subscribe({
        next: (res: any) => {
          console.log(res);

          if (res.error) {
            this.mensaje = res.error;
            return;
          }

          this.mensaje = res.mensaje || 'Operación realizada correctamente ';

          setTimeout(() => {
            this.limpiar();
            this.mensaje = '';
          }, 3000);
        },

        error: (err) => {
          console.error(err);
          this.mensaje = 'Error en la operación';
        },
      });
  }
  id: any = null;

  crearProveedor() {
    this.enviarProveedor('crear');
  }

  editarProveedor() {
    this.enviarProveedor('editar');
  }

  eliminarProveedor() {
    this.enviarProveedor('eliminar');
  }

  limpiar() {
    this.id = null;
    this.nombre = '';
    this.tipo = '';
    this.pais = '';
    this.contacto = '';
  }
  regresar() {
    this.router.navigate(['/operaciones']);
  }
}
