import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-paquetes',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './paquetes.html',
  styleUrl: './paquetes.css',
})

export class Paquetes {
  accion: string = '';
  paquete = {
    nombre: '',
    destinoId: '',
    duracion: '',
    descripcion: '',
    precio: '',
    capacidad: '',
  };

  constructor(private http: HttpClient, private router: Router) {}

  irPaquetes(tipo: string) {
    this.accion = tipo;
  }

  crearPaquete() {

  console.log("DATOS:", this.paquete);

  this.http.post(
    'http://localhost:8080/Proyecto1IPC2/PaqueteServlet',
    this.paquete,
    {
      withCredentials: true
    }
  ).subscribe({
    next: (res: any) => {
      alert("Paquete creado correctamente");
      console.log(res);
    },
    error: (err) => {
      console.error(err);
      alert("Error al crear paquete");
    }
  });
}
 regresar() {
    this.router.navigate(['/operaciones']);
  }
}
