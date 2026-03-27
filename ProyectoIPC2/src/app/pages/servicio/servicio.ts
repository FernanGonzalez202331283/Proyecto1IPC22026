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
  
  constructor(private http: HttpClient, private router: Router) {}

  crearServicio() {
    console.log("SERVICIO:", this.servicio);
    this.http.post(
      'http://localhost:8080/Proyecto1IPC2/ServicioServlet',
      this.servicio,
      {
        withCredentials: true
      }
    ).subscribe({
      next: (res: any) => {
        alert("Servicio creado correctamente");
        console.log(res);
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
