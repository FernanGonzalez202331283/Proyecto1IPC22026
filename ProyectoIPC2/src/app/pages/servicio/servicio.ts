import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-servicio',
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './servicio.html',
  styleUrl: './servicio.css',
})
export class Servicio {
  servicio = {
    paqueteId: '',
    proveedorId: '',
    nombre: '',
    costo: '',
  };
  paquetes: any[] = [];
  proveedores: any[] = [];
  
  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
  this.cargarPaquetes();
  this.cargarProveedores();
}

cargarPaquetes() {
  this.http.get<any[]>(
    'http://localhost:8080/Proyecto1IPC2/PaqueteServlet?accion=listarActivos',
    { withCredentials: true }
  )
  .subscribe(data => {
    this.paquetes = data;
  });
}
cargarProveedores() {
  this.http.get<any[]>(
    'http://localhost:8080/Proyecto1IPC2/ProveedorServlet',
    { withCredentials: true }
  )
  .subscribe(data => {
    this.proveedores = data;
  });
}
  crearServicio() {
  const data = {
    ...this.servicio,
    paqueteId: Number(this.servicio.paqueteId),
    proveedorId: Number(this.servicio.proveedorId),
    costo: Number(this.servicio.costo)
  };

  this.http.post(
    'http://localhost:8080/Proyecto1IPC2/ServicioServlet',
    data,
    { withCredentials: true }
  ).subscribe({
    next: (res: any) => {
      alert("Servicio creado correctamente");
    },
    error: (err) => {
      console.error(err);
      alert("Error al crear servicio");
    }
  });
}
   regresar() {
    this.router.navigate(['/operaciones']);
  }
}
