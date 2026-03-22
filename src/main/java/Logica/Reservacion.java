/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author fernan
 */
public class Reservacion {
    private int id;
    private String fechaViaje;
    private String paquete;
    private int candidadPersonas;
    private String agente;
    private double cosotTotal;
    private String estado;
    private String dpiCliente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public int getCandidadPersonas() {
        return candidadPersonas;
    }

    public void setCandidadPersonas(int candidadPersonas) {
        this.candidadPersonas = candidadPersonas;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public double getCosotTotal() {
        return cosotTotal;
    }

    public void setCosotTotal(double cosotTotal) {
        this.cosotTotal = cosotTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDpiCliente() {
        return dpiCliente;
    }

    public void setDpiCliente(String dpiCliente) {
        this.dpiCliente = dpiCliente;
    }
    
}
