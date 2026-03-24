import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Admin } from './pages/admin/admin';
import { Atencion } from './pages/atencion/atencion';
import { Operaciones } from './pages/operaciones/operaciones';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'admin', component: Admin },
    { path: 'atencion', component: Atencion },
    { path: 'operaciones', component: Operaciones }
];
