import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';

interface Respuesta {
  status: string;
  mensaje: string;
}

@Component({
  selector: 'app-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './usuario.html',
  styleUrls: ['./usuario.css'],
})


export class Usuario {
  
   listaUsuarios: any[] = [];

  nuevo = {
    username: '',
    password: '',
    rol: 'ATENCION'
  };

  constructor(
    private router: Router,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    const rol = localStorage.getItem('rol');

    if (rol !== 'ADMIN') {
      this.router.navigate(['']);
      return;
    }

    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.usuarioService.listar().subscribe({
      next: (res: any[]) => { 
        this.listaUsuarios = res;
      },
      error: () => {
        alert('Error al cargar usuarios');
      }
    });
  }

  crearUsuario() {
    if (!this.nuevo.username || !this.nuevo.password) {
      alert('Completa todos los campos');
      return;
    }

    this.usuarioService.crear(this.nuevo).subscribe({
      next: (res: Respuesta) => { 
        if (res.status === 'ok') {
          alert('Usuario creado');
          this.nuevo = { username: '', password: '', rol: 'ATENCION' };
          this.cargarUsuarios();
        } else {
          alert(res.mensaje || 'Error al crear');
        }
      },
      error: () => {
        alert('Error del servidor');
      }
    });
  }

  cambiarRol(u: any) {
    this.usuarioService.cambiarRol(u.id, u.rol).subscribe({
     next: (res: any) => { 
        if (res.status === 'ok') {
          alert('Rol actualizado');
        } else {
          alert(res.mensaje || 'Error al cambiar rol');
        }
      },
      error: () => {
        alert('Error del servidor');
      }
    });
  }

  cambiarEstado(u: any, estado: boolean) {

    if (u.username === localStorage.getItem('usuario')) {
      alert('No puedes desactivarte a ti mismo');
      return;
    }

    this.usuarioService.cambiarEstado(u.id, estado).subscribe({
     next: (res: any) => { 
        if (res.status === 'ok') {
          u.estado = estado;
        } else {
          alert(res.mensaje || 'Error al cambiar estado');
        }
      },
      error: () => {
        alert('Error del servidor');
      }
    });
  }

  regresar() {
    this.router.navigate(['/admin']);
  }
}
