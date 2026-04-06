/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.List;

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
    private String destino;
    private double totalPagado;
    private double precio;
    private int cupos;
    private String imagen;
    private String fechaCreacion;
    private String agente;
    private List<String> pasajeros;

    public List<String> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<String> pasajeros) {
        this.pasajeros = pasajeros;
    }
    
    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }
    
    
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }
    
    

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }
    
    public int getId() {
        return id;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
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
