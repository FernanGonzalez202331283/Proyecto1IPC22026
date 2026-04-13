import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ReporteService } from '../../services/reporte.service';
import { OnInit } from '@angular/core';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
@Component({
  selector: 'app-reporte',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reporte.html',
  styleUrl: './reporte.css',
})
export class Reporte implements OnInit {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private reporteService: ReporteService,
  ) {}
  paqueteTop: any = null;
  tipo = '';
  inicio = '';
  fin = '';
  ventas: any[] = [];
  ganancias: any = null;
  agente: any = null;
  datos: any[] = [];

  ngOnInit() {
    const rol = localStorage.getItem('rol');

    if (rol !== 'ADMIN') {
      this.router.navigate(['']);
      return;
    }

    this.tipo = this.route.snapshot.paramMap.get('tipo') || '';
  }

  generar() {
    if (!this.inicio || !this.fin) return;
    this.reporteService.obtener(this.tipo, this.inicio, this.fin).subscribe((res) => {
      console.log('RESPUESTA RECIBIDA:', res);
      this.ventas = [];
      this.ganancias = null;
      this.agente = null;
      this.datos = [];
      this.paqueteTop = null;

      // 2. Normalizamos el texto MAYUSCULA/MINUSCULA
      const tipoActivo = this.tipo.toLowerCase();

      if (tipoActivo === 'ventas') {
        this.ventas = res;
      } else if (tipoActivo === 'cancelaciones') {
        this.datos = res;
      } else if (tipoActivo === 'ganancias') {
        this.ganancias = res;
      } else if (tipoActivo.includes('agente')) {
        this.agente = res;
      } else if (tipoActivo.includes('paquete')) {
        this.paqueteTop = res;
      } else if (tipoActivo.includes('ocupacion')) {
        this.datos = res;
      } else {
        this.datos = Array.isArray(res) ? res : [];
      }
    });
  }
  regresar() {
    this.router.navigate(['/admin']);
  }

  exportarPDF() {
    const pdf = new jsPDF();
    let y = 15;
    const tipoActivo = this.tipo.toLowerCase();

    //TITULO PRINCIPAL
    pdf.setFontSize(16);
    pdf.text(`REPORTE: ${this.tipo.toUpperCase()}`, 10, y);
    y += 10;

    pdf.setFontSize(11);
    pdf.text(`Rango: ${this.inicio} - ${this.fin}`, 10, y);
    y += 15;

    // 1. REPORTE DE GANANCIAS
    if (this.ganancias && tipoActivo === 'ganancias') {
      pdf.setFontSize(13);
      pdf.text('RESUMEN DE GANANCIAS', 10, y);
      y += 7;

      pdf.setFontSize(11);
      pdf.text(`Ganancia Bruta: Q${this.ganancias.bruto}`, 10, (y += 7));
      pdf.text(`Reembolsos: Q${this.ganancias.reembolsos}`, 10, (y += 7));
      pdf.setFont('helvetica', 'bold');
      pdf.text(`Ganancia Neta: Q${this.ganancias.neto}`, 10, (y += 7));
      pdf.setFont('helvetica', 'normal');
      y += 10;
    }
    // 2. REPORTE DE VENTAS
    if (this.ventas?.length > 0 && tipoActivo === 'ventas') {
      autoTable(pdf, {
        startY: y,
        head: [['Agente', 'Paquete', 'Total', 'Pasajeros']],
        body: this.ventas.map((v: any) => [v.agente, v.paquete, `Q${v.total}`, v.pasajeros]),
      });
      y = (pdf as any).lastAutoTable.finalY + 15;
    }

    // 3. REPORTE DE MEJOR AGENTE
    else if (tipoActivo.includes('agente') && this.agente) {
      pdf.setFontSize(13);
      pdf.text(`MEJOR AGENTE: ${this.agente.agente}`, 10, y);
      pdf.text(`Total: Q${this.agente.total}`, 120, y);
      y += 7;

      if (this.agente.reservaciones) {
        autoTable(pdf, {
          startY: y,
          head: [['Reservación', 'Paquete', 'Personas', 'Monto']],
          body: this.agente.reservaciones.map((r: any) => [
            r.reservacion,
            r.paquete,
            r.personas,
            `Q${r.monto}`,
          ]),
        });
        y = (pdf as any).lastAutoTable.finalY + 15;
      }
    }
    // 4. REPORTE DE PAQUETE MAS VENDIDO O MENOS VENDIDO)
    else if (tipoActivo.includes('paquete') && this.paqueteTop) {
      const titulo =
        tipoActivo.includes('min') || tipoActivo.includes('menos')
          ? 'PAQUETE MENOS VENDIDO'
          : 'PAQUETE MÁS VENDIDO';

      pdf.setFontSize(13);
      pdf.text(`${titulo}: ${this.paqueteTop.paquete}`, 10, y);
      y += 7;
      pdf.text(`Total Reservas: ${this.paqueteTop.total_reservas}`, 10, y);
      y += 5;

      if (this.paqueteTop.reservaciones?.length > 0) {
        autoTable(pdf, {
          startY: y,
          head: [['ID', 'Viaje', 'Pax', 'Total', 'Estado', 'Cliente']],
          body: this.paqueteTop.reservaciones.map((r: any) => [
            `#${r.reservacion}`,
            r.fecha_viaje,
            r.personas,
            `Q${r.total}`,
            r.estado,
            r.cliente,
          ]),
        });
        y = (pdf as any).lastAutoTable.finalY + 15;
      }
    }
    // 5. ocupacion destino reporte
    else if (tipoActivo.includes('ocupacion') && this.datos?.length > 0) {
      pdf.setFontSize(13);
      pdf.text('OCUPACIÓN POR DESTINO', 10, y);

      autoTable(pdf, {
        startY: y + 5,
        head: [['Destino', 'País', 'Cantidad de Reservaciones']],
        body: this.datos.map((d: any) => [d.destino, d.pais || 'N/A', d.cantidad]),
      });
      y = (pdf as any).lastAutoTable.finalY + 15;
    }
    // 6 cancelaciones
    else if (tipoActivo === 'cancelaciones' && this.datos?.length > 0) {
      pdf.setFontSize(13);
      pdf.text('DETALLE DE CANCELACIONES', 10, y);

      autoTable(pdf, {
        startY: y + 5,
        head: [['Reservación', 'Fecha', 'Reembolso', 'Pérdida']],
        body: this.datos.map((d: any) => [
          d.reservacion,
          d.fecha,
          `Q${d.reembolso}`,
          `Q${d.perdida}`,
        ]),
      });
    }

    pdf.save(`reporte_${this.tipo}.pdf`);
  }
}
