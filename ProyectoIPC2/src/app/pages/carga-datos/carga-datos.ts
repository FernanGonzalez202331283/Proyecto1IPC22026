import { Component } from '@angular/core';
import { Router } from '@angular/router'; 
@Component({
  selector: 'app-carga-datos',
  imports: [],
  templateUrl: './carga-datos.html',
  styleUrl: './carga-datos.css',
})
export class CargaDatos {
  constructor(private router: Router) {}
  
  ngOnInit() {
  const rol = localStorage.getItem('rol');

  if (rol !== 'admin') {
    this.router.navigate(['']);
  }
}
}
