/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites.views;

import java.util.Date;

/**
 *
 * @author Asullom
 */
public class ComprasProveMov {
    private int id;
    private Date fecha;
    private int prove_id;
    private String prove_nom;
    private double ingreso_cant_gr;

    private double onza;
    private double porc;
    private double ley;
    private double sistema;
    private double tcambio;
    private double precio_do;
    private double precio_so;

    private double total_do;
    private double total_so;
    private double saldo_porpagar_do;
    private double saldo_porpagar_so;


    private double egreso_do;
    private double egreso_so;
    private double ingreso_do;
    private double ingreso_so;
    
    private String glosa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getProve_id() {
        return prove_id;
    }

    public void setProve_id(int prove_id) {
        this.prove_id = prove_id;
    }

    public String getProve_nom() {
        return prove_nom;
    }

    public void setProve_nom(String prove_nom) {
        this.prove_nom = prove_nom;
    }

    public double getIngreso_cant_gr() {
        return ingreso_cant_gr;
    }

    public void setIngreso_cant_gr(double ingreso_cant_gr) {
        this.ingreso_cant_gr = ingreso_cant_gr;
    }

    public double getOnza() {
        return onza;
    }

    public void setOnza(double onza) {
        this.onza = onza;
    }

    public double getPorc() {
        return porc;
    }

    public void setPorc(double porc) {
        this.porc = porc;
    }

    public double getLey() {
        return ley;
    }

    public void setLey(double ley) {
        this.ley = ley;
    }

    public double getSistema() {
        return sistema;
    }

    public void setSistema(double sistema) {
        this.sistema = sistema;
    }

    public double getTcambio() {
        return tcambio;
    }

    public void setTcambio(double tcambio) {
        this.tcambio = tcambio;
    }

    public double getPrecio_do() {
        return precio_do;
    }

    public void setPrecio_do(double precio_do) {
        this.precio_do = precio_do;
    }

    public double getPrecio_so() {
        return precio_so;
    }

    public void setPrecio_so(double precio_so) {
        this.precio_so = precio_so;
    }

    public double getTotal_do() {
        return total_do;
    }

    public void setTotal_do(double total_do) {
        this.total_do = total_do;
    }

    public double getTotal_so() {
        return total_so;
    }

    public void setTotal_so(double total_so) {
        this.total_so = total_so;
    }

    public double getSaldo_porpagar_do() {
        return saldo_porpagar_do;
    }

    public void setSaldo_porpagar_do(double saldo_porpagar_do) {
        this.saldo_porpagar_do = saldo_porpagar_do;
    }

    public double getSaldo_porpagar_so() {
        return saldo_porpagar_so;
    }

    public void setSaldo_porpagar_so(double saldo_porpagar_so) {
        this.saldo_porpagar_so = saldo_porpagar_so;
    }

    public double getEgreso_do() {
        return egreso_do;
    }

    public void setEgreso_do(double egreso_do) {
        this.egreso_do = egreso_do;
    }

    public double getEgreso_so() {
        return egreso_so;
    }

    public void setEgreso_so(double egreso_so) {
        this.egreso_so = egreso_so;
    }

    public double getIngreso_do() {
        return ingreso_do;
    }

    public void setIngreso_do(double ingreso_do) {
        this.ingreso_do = ingreso_do;
    }

    public double getIngreso_so() {
        return ingreso_so;
    }

    public void setIngreso_so(double ingreso_so) {
        this.ingreso_so = ingreso_so;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

   
    
}
