import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ReporteService } from '../../services/reporte.service';
import { OnInit } from '@angular/core';
@Component({
  selector: 'app-reporte',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reporte.html',
  styleUrl: './reporte.css',
})

export class Reporte implements OnInit{
  constructor(
  private router: Router,
  private route: ActivatedRoute,
  private reporteService: ReporteService

) {}

tipo = '';
  ngOnInit() {
  const rol = localStorage.getItem('rol');

  if (rol !== 'ADMIN') {
    this.router.navigate(['']);
  }
  this.tipo = this.route.snapshot.paramMap.get('tipo') || '';
}

inicio = '';
fin = '';
datos: any[] = [];

generar() {
  if (!this.inicio || !this.fin) {
    console.log("Fechas vacías");
    return;
  }

  this.reporteService.obtener(this.tipo, this.inicio, this.fin)
    .subscribe(res => {
      console.log(res);
      this.datos = res;
    });
}
}
