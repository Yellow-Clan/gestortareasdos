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
public class ProveMov {

    private int id;
    private Date fecha;
    private int prove_id;
    private String prove_nom;
    private String glosa;
    private int esdolares;
    private int esadelanto;
    private double adelanto_do;
    private double adelanto_so;
    private double cobro_do;
    private double cobro_so;

    private int user;
    private int activo;
    private Date date_created;
    private Date last_updated;

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

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

    public int getEsdolares() {
        return esdolares;
    }

    public void setEsdolares(int esdolares) {
        this.esdolares = esdolares;
    }

    public int getEsadelanto() {
        return esadelanto;
    }

    public void setEsadelanto(int esadelanto) {
        this.esadelanto = esadelanto;
    }

    public double getAdelanto_do() {
        return adelanto_do;
    }

    public void setAdelanto_do(double adelanto_do) {
        this.adelanto_do = adelanto_do;
    }

    public double getAdelanto_so() {
        return adelanto_so;
    }

    public void setAdelanto_so(double adelanto_so) {
        this.adelanto_so = adelanto_so;
    }

    public double getCobro_do() {
        return cobro_do;
    }

    public void setCobro_do(double cobro_do) {
        this.cobro_do = cobro_do;
    }

    public double getCobro_so() {
        return cobro_so;
    }

    public void setCobro_so(double cobro_so) {
        this.cobro_so = cobro_so;
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
