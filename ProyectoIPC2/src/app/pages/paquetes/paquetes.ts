import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-paquetes',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './paquetes.html',
  styleUrl: './paquetes.css',
})

export class Paquetes implements OnInit{
  
  accion: string = 'menu';
  mensaje: string = '';

  paquete: any = {
    accion: '',
    id: null,
    nombre: '',
    destinoId: '',
    duracion: '',
    descripcion: '',
    precio: '',
    capacidad: '',
  };

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  // ================= INIT =================
  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.accion = params['accion'] || 'menu';
    });
  }

  // ================= CAMBIAR VISTA =================
  irPaquetes(tipo: string) {
  this.accion = tipo;
  this.mensaje = '';

  if (tipo === 'crear') {
    this.limpiar();
  }
}

  enviarPaquete(accion: string) {
    if ((accion === 'activar' || accion === 'desactivar') && !this.paquete.id) {
  this.mensaje = 'ID requerido';
  return;
}

  // validar sin convertir
  if (accion !== 'crear' && (!this.paquete.id || this.paquete.id.toString().trim() === '')) {
    this.mensaje = 'ID requerido';
    return;
  }

  if (accion === 'crear' && (!this.paquete.nombre || !this.paquete.destinoId)) {
    this.mensaje = 'Complete los campos obligatorios';
    return;
  }
  
   if (accion === 'editar') {
    if (!this.paquete.nombre ||
        !this.paquete.destinoId ||
        !this.paquete.duracion ||
        !this.paquete.precio ||
        !this.paquete.capacidad) {

      this.mensaje = 'Debe llenar TODOS los campos para editar';
      return;
    }
  }

  // crear objeto nuevo para enviar
  const data = {
    ...this.paquete, // copia todo lo que haya
    id: Number(this.paquete.id),
    destinoId: Number(this.paquete.destinoId),
    duracion: Number(this.paquete.duracion),
    precio: Number(this.paquete.precio),
    capacidad: Number(this.paquete.capacidad),
    accion: accion
  };

  this.http.post(
    'http://localhost:8080/Proyecto1IPC2/PaqueteServlet',
    data,
    { withCredentials: true }
  ).subscribe({
  next: (res: any) => {

    console.log(res); // para ver respuesta

    //manejar error del backend
    if (res.error) {
      this.mensaje = res.error;
      return;
    }

    //mensaje correcto
    this.mensaje = res.mensaje || 'Operación realizada correctamente ';

    // esperar antes de limpiar
    setTimeout(() => {
      this.limpiar();   // ahora sí
      this.mensaje = '';
    }, 3000);
  },

  error: (err) => {
    console.error(err);
    this.mensaje = 'Error en la operación';
  }
});
}

  // ================= ACCIONES =================
  crearPaquete() {
    this.enviarPaquete('crear');
  }

  editarPaquete() {
    this.enviarPaquete('editar');
  }

  desactivarPaquete() {
    this.enviarPaquete('desactivar');
  }

  // ================= LIMPIAR =================
  limpiar() {
    this.paquete = {
      accion: '',
      id: null,
      nombre: '',
      destinoId: '',
      duracion: '',
      descripcion: '',
      precio: '',
      capacidad: '',
    };
  }

  // ================= REGRESAR =================
  regresar() {
    this.router.navigate(['/operaciones']);
  }
  activarPaquete() {
    this.enviarPaquete('activar');
  }
}
