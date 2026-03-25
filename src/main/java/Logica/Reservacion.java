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
    private int idUsurio;
    private double cosotTotal;
    private String estado;
    private String[] dpis;
    private int paqueteId;

    public int getId() {
        return id;
    }

    public String[] getDpis() {
        return dpis;
    }

    public void setDpis(String[] dpis) {
        this.dpis = dpis;
    }

    public int getIdUsurio() {
        return idUsurio;
    }

    public void setIdUsurio(int idUsurio) {
        this.idUsurio = idUsurio;
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
    public int getPaqueteId() {
        return paqueteId;
    }

    public void setPaqueteId(int paqueteId) {
        this.paqueteId = paqueteId;
    }
    
}
