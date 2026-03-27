import { Component } from '@angular/core';
import { Router } from '@angular/router'; 
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
@Component({
  selector: 'app-proveedores',
  imports: [HttpClientModule, CommonModule, FormsModule],
  templateUrl: './proveedores.html',
  styleUrl: './proveedores.css',
})
export class Proveedores {
   nuevoProveedor = {
    nombre: '',
    tipo: '',
    pais: '',
    contacto: ''
  };

  constructor(private http: HttpClient, private router: Router) {}

  irProveedores(accion: string) {
    if (accion === 'crear') {
      // Validar campos básicos
      if (!this.nuevoProveedor.nombre || !this.nuevoProveedor.tipo || !this.nuevoProveedor.pais || !this.nuevoProveedor.contacto) {
        alert('Todos los campos son requeridos');
        return;
      }

      const body = new HttpParams()
        .set('accion', 'crear')
        .set('nombre', this.nuevoProveedor.nombre)
        .set('tipo', this.nuevoProveedor.tipo)
        .set('pais', this.nuevoProveedor.pais)
        .set('contacto', this.nuevoProveedor.contacto);

      this.http.post('http://localhost:8080/Proyecto1IPC2/ProveedorServlet', body.toString(), {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        withCredentials: true // Importante si tu servlet usa sesión
      }).subscribe({
        next: (res: any) => {
          alert(res.mensaje || 'Proveedor creado con éxito');
          // Limpiar formulario
          this.nuevoProveedor = { nombre: '', tipo: '', pais: '', contacto: '' };
        },
        error: (err) => {
          console.error(err);
          alert('Error al crear proveedor');
        }
      });
    }
  }
}
