import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Admin } from './pages/admin/admin';
import { Atencion } from './pages/atencion/atencion';
import { Operaciones } from './pages/operaciones/operaciones';
import { Clientes } from './pages/clientes/clientes';
import { Reservaciones } from './pages/reservaciones/reservaciones';
import { Pagos } from './pages/pagos/pagos';
import { Reembolsos } from './pages/reembolsos/reembolsos';
import { Destinos } from './pages/destinos/destinos';
import { Paquetes } from './pages/paquetes/paquetes';
import { Proveedores } from './pages/proveedores/proveedores';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'admin', component: Admin },
    { path: 'atencion', component: Atencion },
    { path: 'operaciones', component: Operaciones },
    { path: 'clientes', component: Clientes },
    { path: 'reservaciones', component: Reservaciones },
    { path: 'pagos', component: Pagos },
    { path: 'reembolsos', component: Reembolsos },
    { path: 'destinos', component: Destinos},
    { path: 'paquetes', component: Paquetes},
    { path: 'proveedores', component: Proveedores}
];
