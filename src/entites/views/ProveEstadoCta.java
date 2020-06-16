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
public class ProveEstadoCta {
    private int id;
    private Date fecha;
    private int prove_id;
    private String prove_nom;
    private String glosa;
    private double debito_do;
    private double debito_so;
    private double credito_do;
    private double credito_so;

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

    public double getDebito_do() {
        return debito_do;
    }

    public void setDebito_do(double debito_do) {
        this.debito_do = debito_do;
    }

    public double getDebito_so() {
        return debito_so;
    }

    public void setDebito_so(double debito_so) {
        this.debito_so = debito_so;
    }

    public double getCredito_do() {
        return credito_do;
    }

    public void setCredito_do(double credito_do) {
        this.credito_do = credito_do;
    }

    public double getCredito_so() {
        return credito_so;
    }

    public void setCredito_so(double credito_so) {
        this.credito_so = credito_so;
    }

    
}
