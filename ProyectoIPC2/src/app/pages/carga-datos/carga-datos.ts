import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-carga-datos',
  imports: [CommonModule, FormsModule],
  templateUrl: './carga-datos.html',
  styleUrl: './carga-datos.css',
})
export class CargaDatos {
  archivo!: File;
  mensaje: string = '';

  constructor(
    private router: Router,
    private http: HttpClient,
  ) {}

  ngOnInit() {
    const rol = localStorage.getItem('rol');

    if (rol !== 'ADMIN') {
      this.router.navigate(['']);
    }
  }

  seleccionarArchivo(event: any) {
    this.archivo = event.target.files[0];
  }

  subirArchivo() {
    if (!this.archivo) {
      this.mensaje = 'Selecciona un archivo primero';
      return;
    }

    const formData = new FormData();
    formData.append('archivo', this.archivo);

    this.http.post('http://localhost:8080/Proyecto1IPC2/carga', formData).subscribe({
      next: (res: any) => {
        this.mensaje = 'Archivo procesado correctamente';
      },
      error: (err) => {
        this.mensaje = 'Error al subir archivo';
      },
    });
  }
  regresar() {
    this.router.navigate(['/admin']);
  }
}
