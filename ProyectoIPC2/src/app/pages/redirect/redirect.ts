import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-redirect',
  imports: [],
  templateUrl: './redirect.html',
  styleUrl: './redirect.css',
})
export class Redirect {
  constructor(private router: Router) {
  const last = sessionStorage.getItem('lastRoute');

  if (last && last !== this.router.url) {
    this.router.navigateByUrl(last);
  } else {
    this.router.navigate(['/']); // Redirige a la página principal si no hay ruta previa
  }
}
}
