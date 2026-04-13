import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
@Component({
  selector: 'app-pagos',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './pagos.html',
  styleUrl: './pagos.css',
})
export class Pagos {
  constructor(private router: Router) {}

  reservacion_id: number = 0;

  reservacion: any = null;

  monto: number = 0;
  metodo: string = '';

  historial: any[] = [];

  totalPagado: number = 0;

  // BUSCAR RESERVACIÓN
  buscarReservacion() {
    if (!this.reservacion_id) {
      alert('Ingrese ID');
      return;
    }

    fetch(`http://localhost:8080/Proyecto1IPC2/ReservacionServlet?id=${this.reservacion_id}`, {
      method: 'GET',
      credentials: 'include',
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error('No encontrada');
        }
        return res.json();
      })
      .then((data) => {
        if (data.error) {
          alert(data.error);
          this.reservacion = null;
        } else {
          this.reservacion = data;
          this.totalPagado = data.totalPagado || 0;
          this.historial = data.pagos || [];
        }
      })
      .catch((err) => {
        alert('Reservación no encontrada o error en servidor');
        this.reservacion = null;
      });
  }

  // REGISTRAR PAGO
  registrarPago() {
    if (!this.monto || !this.metodo) {
      alert('Complete los datos');
      return;
    }

    fetch('http://localhost:8080/Proyecto1IPC2/PagoServlet', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        reservacion_id: this.reservacion_id,
        monto: this.monto,
        metodo: this.metodo,
      }),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.error) {
          alert(data.error);
        } else {
          alert('Pago registrado');

         // LIMPIAR CAMPOS
    this.monto = 0;
    this.metodo = '';

    // ACTUALIZAR 
    if (this.reservacion) {
      this.reservacion.totalPagado += data.monto || 0;

      // si ya pagó todo
      if (this.reservacion.totalPagado >= this.reservacion.cosotTotal) {
        this.reservacion.estado = 'CONFIRMADA';
      }
    }
    this.buscarReservacion();
        }
      });
  }

  generarPDF() {
    if (!this.reservacion) {
      alert('No hay datos');
      return;
    }

    const doc = new jsPDF();

    doc.setFontSize(18);
    doc.text('CONSTANCIA DE PAGO', 60, 20);

    doc.setFontSize(12);

    doc.text(`ID Reservación: ${this.reservacion.id}`, 20, 40);
    doc.text(`Paquete: ${this.reservacion.paquete}`, 20, 50);
    doc.text(`Destino: ${this.reservacion.destino}`, 20, 60);
    doc.text(`Fecha viaje: ${this.reservacion.fechaViaje}`, 20, 70);
    doc.text(`Personas: ${this.reservacion.candidadPersonas}`, 20, 80);

    doc.text(`Total: Q${this.reservacion.cosotTotal}`, 20, 100);
    doc.text(`Pagado: Q${this.totalPagado}`, 20, 110);
    doc.text(`Estado: ${this.reservacion.estado}`, 20, 120);

    doc.setFontSize(10);
    doc.text('Gracias por su compra ', 70, 150);

    doc.save(`Pago_Reservacion_${this.reservacion.id}.pdf`);
  }
  regresar() {
    this.router.navigate(['/atencion']);
  }
}
