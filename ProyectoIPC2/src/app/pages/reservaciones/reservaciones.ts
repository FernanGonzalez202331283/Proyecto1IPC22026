import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-reservaciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservaciones.html',
  styleUrl: './reservaciones.css',
})
export class Reservaciones {

  accion: string = '';
  historial: any[] = [];
  // PAQUETES
  paquetes: any[] = [];
  paqueteSeleccionado: any = null;
  dpiBuscar: string = '';
  // DATOS RESERVACIÓN
  fecha_viaje: string = '';
  cantidad: number = 1;
  costo: number = 0;
  destinos: any[] = [];
  destinoSeleccionado: any = null;
  fechaBuscar: string = '';
  disponibles: any[] = [];
  // PASAJEROS
  dpis: string[] = [''];
  clientes: any[] = [{}];

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
     this.route.queryParams.subscribe(params => {
    this.accion = params['accion'];

    // LIMPIAR DATOS SI CAMBIA VISTA
    this.historial = [];
    this.disponibles = [];

    if (this.accion === 'crear') {
      this.cargarPaquetes();
    }

    if (this.accion === 'disponibles') {
      this.cargarDestinos();
    }
    if (this.accion === 'hoy') {
      this.cargarHoy();
    }
  });

  }

  cargarHoy() {
  fetch('http://localhost:8080/Proyecto1IPC2/ReservacionServlet?accion=hoy', {
    method: 'GET',
    credentials: 'include'
  })
  .then(res => res.json())
  .then(data => {
    console.log("Reservaciones hoy:", data);
    this.disponibles = data; // reutilizamos la tabla
  })
  .catch(err => console.error(err));
}

mostrarHoy() {
  this.accion = 'hoy';
  this.cargarHoy();
}
  cargarDestinos() {
  fetch('http://localhost:8080/Proyecto1IPC2/DestinoServlet', {
    method: 'GET',
    credentials: 'include'
  })
  .then(res => res.json())
  .then(data => {
    console.log("Destinos:", data);
    this.destinos = data;
  })
  .catch(err => console.error(err));
}

cargarDisponibles() {

  if (!this.fechaBuscar) {
    alert("Seleccione fecha");
    return;
  }

  fetch(`http://localhost:8080/Proyecto1IPC2/ReservacionServlet?accion=disponibles&fecha=${this.fechaBuscar}`, {
    method: 'GET',
    credentials: 'include'
  })
  .then(res => res.json())
  .then(data => {
    console.log("Disponibles:", data);
    this.disponibles = data;
  })
  .catch(err => console.error(err));
}

cargarHistorial() {

  if (!this.dpiBuscar || this.dpiBuscar.trim() === '') {
    alert("Ingrese un DPI");
    return;
  }

  fetch(`http://localhost:8080/Proyecto1IPC2/ReservacionServlet?accion=historialCliente&dpi=${this.dpiBuscar}`, {
    method: 'GET',
    credentials: 'include'
  })
  .then(res => res.json())
  .then(data => {
    console.log("Historial:", data);
    this.historial = data;
  })
  .catch(err => console.error(err));
}

  // OBTENER PAQUETES
  cargarPaquetes() {
    fetch('http://localhost:8080/Proyecto1IPC2/PaqueteServlet', {
      method: 'GET',
      credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {
      console.log("Paquetes:", data);
      this.paquetes = data;
    })
    .catch(err => console.error(err));
  }

  // CALCULAR COSTO
  calcularCosto() {
    this.costo = this.paqueteSeleccionado?.precio
      ? this.paqueteSeleccionado.precio * this.dpis.length
      : 0;
  }

  // AGREGAR PASAJERO
  agregarPasajero() {
    this.dpis.push('');
    this.clientes.push({});
    this.cantidad = this.dpis.length;
    this.calcularCosto();
  }

  // ELIMINAR PASAJERO
  eliminarPasajero(index: number) {
    this.dpis.splice(index, 1);
    this.clientes.splice(index, 1);
    this.cantidad = this.dpis.length;
    this.calcularCosto();
  }

  // BUSCAR CLIENTE
  buscarCliente(index: number) {

    const dpi = this.dpis[index];

    if (!dpi || dpi.trim() === '') {
      alert("Ingrese DPI");
      return;
    }

    fetch(`http://localhost:8080/Proyecto1IPC2/ClienteServlet?dpi=${dpi}`, {
      method: 'GET',
      credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {

      console.log("Cliente:", data);

      if (data.mensaje === "Cliente no encontrado") {

        const ir = confirm("Cliente no existe ¿Desea registrarlo?");

        if (ir) {
          this.router.navigate(['/clientes'], {
            queryParams: {
              accion: 'registrar',
              dpi: dpi
            }
          });
        }

        this.clientes[index] = {};

      } else {
        this.clientes[index] = data;
      }

    })
    .catch(err => console.error(err));
  }

  // CREAR RESERVACIÓN
  crearReservacion() {

    //VALIDACIONES
    if (!this.paqueteSeleccionado?.id) {
      alert("Seleccione un paquete válido");
      return;
    }

    if (!this.fecha_viaje) {
      alert("Seleccione fecha");
      return;
    }

    if (this.dpis.length === 0) {
      alert("Debe agregar pasajeros");
      return;
    }

    for (let dpi of this.dpis) {
      if (!dpi || dpi.trim() === '') {
        alert("Hay DPI vacíos");
        return;
      }
    }

    //FORMATEO CORRECTO DE FECHA
    const fechaObj = new Date(this.fecha_viaje);

    if (isNaN(fechaObj.getTime())) {
      alert("Fecha inválida");
      return;
    }

    const fechaFormateada = fechaObj.toISOString().split('T')[0];

    //DEBUG CLAVE
    console.log("=== DEBUG RESERVACIÓN ===");
    console.log("Fecha enviada:", fechaFormateada);
    console.log("Paquete ID:", this.paqueteSeleccionado.id);
    console.log("Cantidad:", this.cantidad);
    console.log("Costo:", this.costo);
    console.log("DPIs:", this.dpis);

    const formData = new FormData();

    formData.append('fecha_viaje', fechaFormateada);
    formData.append('paquete_id', this.paqueteSeleccionado.id.toString());
    formData.append('cantidad', this.cantidad.toString());
    formData.append('costo', this.costo.toString());

    this.dpis.forEach(dpi => {
      formData.append('dpis', dpi);
    });

    //DEBUG FINAL (MUY IMPORTANTE)
    for (let pair of formData.entries()) {
      console.log(pair[0] + ': ' + pair[1]);
    }

  fetch('http://localhost:8080/Proyecto1IPC2/ReservacionServlet', {
    method: 'POST',
    credentials: 'include',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify({
      fecha_viaje: fechaFormateada,
      paquete_id: this.paqueteSeleccionado.id,
      cantidad: this.cantidad,
      costo: this.costo,
      dpis: this.dpis
    })
  })
    .then(res => res.json())
    .then(data => {

      console.log("Respuesta:", data);

      if (data.error) {
        alert(data.error);
      } else {
        alert(data.mensaje || "Reservación creada correctamente");
        this.resetFormulario();
      }

    })
    .catch(error => {
      console.error(error);
      alert("Error al crear reservación");
    });
  }

  // LIMPIAR
  resetFormulario() {
    this.paqueteSeleccionado = null;
    this.fecha_viaje = '';
    this.cantidad = 1;
    this.costo = 0;
    this.dpis = [''];
    this.clientes = [{}];
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }
  
  regresar() {
    this.router.navigate(['/atencion']);
  }
}
