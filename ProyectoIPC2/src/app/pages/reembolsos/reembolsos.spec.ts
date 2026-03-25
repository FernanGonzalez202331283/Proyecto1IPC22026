import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reembolsos } from './reembolsos';

describe('Reembolsos', () => {
  let component: Reembolsos;
  let fixture: ComponentFixture<Reembolsos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reembolsos],
    }).compileComponents();

    fixture = TestBed.createComponent(Reembolsos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
