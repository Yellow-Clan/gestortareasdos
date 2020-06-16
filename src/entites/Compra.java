/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

import java.util.Date;

/**
 *
 * @author Asullom
 */
public class Compra {

    private int id;
    private Date fecha;
    private int prove_id;
    private String prove_nom;
    private double cant_gr;
    private int esdolares;

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
    private int user;
    private int activo;
    private Date date_created;
    private Date last_updated;

    //Datos para visualizar
    private double egreso_do;//total_do-saldo_porpagar_do
    private double egreso_so;//total_so-saldo_porpagar_so

    public double getEgreso_do() {
        return total_do - saldo_porpagar_do;
    }

    public double getEgreso_so() {
        return total_so - saldo_porpagar_so;
    }

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

    public double getCant_gr() {
        return cant_gr;
    }

    public void setCant_gr(double cant_gr) {
        this.cant_gr = cant_gr;
    }

    public int getEsdolares() {
        return esdolares;
    }

    public void setEsdolares(int esdolares) {
        this.esdolares = esdolares;
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

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }

}
