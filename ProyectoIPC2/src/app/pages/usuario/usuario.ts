import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';
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
    }

    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.usuarioService.listar().subscribe(res => {
      this.listaUsuarios = res;
    });
  }

  crearUsuario() {
    this.usuarioService.crear(this.nuevo).subscribe(res => {
      if (res.status === 'ok') {
        alert('Usuario creado');
        this.cargarUsuarios();
      } else {
        alert('Error al crear');
      }
    });
  }

  regresar() {
    this.router.navigate(['/admin']);
  }
}
