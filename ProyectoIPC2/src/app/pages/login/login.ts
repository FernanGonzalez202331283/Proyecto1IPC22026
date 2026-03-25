import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Auth } from '../../services/auth';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {

  username: string = '';
  password: string = '';

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  login() {
    this.authService.login(this.username, this.password)
      .subscribe({
        next: (res) => {
          if (res.status === 'ok') {
            localStorage.setItem('usuario', res.usuario);
            localStorage.setItem('rol', res.rol);
            const rol = res.rol.toLowerCase();
            this.router.navigate(['/' + rol]);
            alert('Bienvenido ' + res.usuario);
          } else {
            alert(res.mensaje);
          }
        },
        
        error: (err) => {
          console.error(err);
        }
      });
      
  }
}