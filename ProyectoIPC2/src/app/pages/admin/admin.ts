import { Component } from '@angular/core';
import { Atencion } from "../atencion/atencion";
import { Operaciones } from "../operaciones/operaciones";

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [Atencion, Operaciones],
  templateUrl: './admin.html',
  styleUrl: './admin.css',
})
export class Admin {}
