/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.util;

/*
 * PrintTicketera.java
 *(c) 2004 Angel Sullon Macalupu, Todos los derechos reservados
 */
import data.CompraData;
import data.Conn;
import data.ProveMovData;
import entites.Compra;
import entites.ProveMov;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.print.PrintException;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PrintTicketera implements Printable {

    static List<Fila> doc_filas = new CopyOnWriteArrayList<Fila>();

    public PrintTicketera(List<Fila> lis) {
        this.doc_filas = lis;
    }

    public int print(Graphics g, PageFormat pf, int pageIndex)
            throws PrinterException {
        Graphics2D g2 = (Graphics2D) g;
        for (Fila d : doc_filas) {
            g2.setFont(new Font(d.getFuente(), Font.PLAIN, d.getTam()));
            g2.drawString(d.getCadena(), d.getX(), d.getY());
        }
        return Printable.PAGE_EXISTS;
    }

    static String getDestination(PrintRequestAttributeSet attrs)
            throws PrintException {
        if (attrs != null) {
            if (attrs.containsKey(Destination.class)) {
                Destination destination
                        = (Destination) attrs.get(Destination.class);
                if (!destination.getURI().getScheme().equals("file")) {
                    throw new PrintException(
                            "Only files supported as destinations.");
                }
                String file = destination.getURI().getPath();
                if (file.startsWith("/")) {
                    file = file.substring(1);
                }
                return file;
            }
        }
        return null;
    }

    static File createTempFile(String prefix, String suffix) {
        String tempDir = System.getProperty("user.home");
        String fileName = (prefix != null ? prefix : "") + System.nanoTime() + (suffix != null ? suffix : "");
        return new File(tempDir, fileName);
    }

    public static void imprime(List<Fila> lis) {

        PrinterJob job = PrinterJob.getPrinterJob();
        Paper pp = new Paper();
        pp.setSize(800, 1200);
        pp.setImageableArea(0, 0, 800, 1200);
        PageFormat mipag = job.defaultPage();
        mipag.setOrientation(PageFormat.PORTRAIT);//vertical
        //mipag.setOrientation(PageFormat.LANDSCAPE);//horizontal
        //mipag = job.pageDialog(mipag);
        mipag.setPaper(pp);
        Book bk = new Book();
        bk.append(new PrintTicketera(lis), mipag);
        job.setPageable(bk);
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        //if (job.printDialog()) { //if (job.printDialog(attributes)) {
        try {

            // job.print();
            //if (job.cancel() ){
            //}
            /*
                try {
                    PrintService prtSrv = job.getPrintService();
                    if (job.getPrintService() == null) {
                        System.out.println("No printers. Test cannot continue");
                        return;
                    }
                    if (!prtSrv.isAttributeCategorySupported(JobSheets.class)) {
                        System.out.println("No prtSrve");
                        return;
                    }else  {
                        System.out.println("si prtSrve");
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/
            try {
                attributes.add(new Destination(createTempFile("", ".pdf").toURI()));
                System.out.println(" " + getDestination(attributes));
            } catch (Exception e) {
                e.printStackTrace();
            }
            job.print(attributes);
            try {
                if (getDestination(attributes) != null) {
                    Desktop.getDesktop().open(new File(getDestination(attributes)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (PrinterException e2) { // Handle Exception 
            System.out.println(e2);
        }
       //  }
    }

    /*
        switch (tab) {
            case "a":
                System.out.println("****** a *****");

                break;

            default:
                System.out.println("Incorrecto");
                break;
        }*/
    public static void imp_compra(int id) {

        Compra rc = CompraData.getById(id);
        System.out.println("rc.getProve_nom=" + rc.getProve_nom());

        SimpleDateFormat sdf = new SimpleDateFormat(Conn.DEFAULT_DATE_STRING_FORMAT);
        SimpleDateFormat sdf2 = new SimpleDateFormat(Conn.DEFAULT_DATE_STRING_FORMAT_PE);

        Date dt = new Date();
        String currentTime = sdf.format(dt);

        List<Fila> lis = new CopyOnWriteArrayList<Fila>();
        int i = 20;
        lis.add(new Fila(80, i = i + 20, ""));
        lis.add(new Fila(80 + 20, i = i + 20, "M&G Negocios", 14, "Taahoma"));
        lis.add(new Fila(80, i = i + 20, "=== COMPRA DE MATERIAL ==="));
        lis.add(new Fila(80, i = i + 20, "NUM:" + String.format("%04d", rc.getId()) + "  FECHA:" + sdf2.format(dt)));
        lis.add(new Fila(80, i = i + 10, ""));
        lis.add(new Fila(80, i = i + 20, "DE: " + rc.getProve_nom()));

        lis.add(new Fila(80, i = i + 20, "GRAMOS: " + new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(rc.getCant_gr())));
        String moneda = "SIN MONEDA";
        double precio = 0;
        double total = 0;
        double saldo_porpagar = 0;

        if (rc.getEsdolares() == 1) {
            moneda = "EN DÓLARES";
            precio = rc.getPrecio_do();
            total = rc.getTotal_do();
            saldo_porpagar = rc.getSaldo_porpagar_do();
        }
        if (rc.getEsdolares() == 0) {
            moneda = " EN SOLES";
            precio = rc.getPrecio_so();
            total = rc.getTotal_so();
            saldo_porpagar = rc.getSaldo_porpagar_so();
        }
        lis.add(new Fila(80, i = i + 20, "MONEDA: " + moneda));
        lis.add(new Fila(80, i = i + 20, "PRECIO: " + new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(precio)));
        lis.add(new Fila(80, i = i + 20, "TOTAL: " + new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(total)));
        lis.add(new Fila(80, i = i + 20, ""));
        lis.add(new Fila(80, i = i + 20, "POR PAGAR: " + new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(saldo_porpagar)));

        lis.add(new Fila(80, i = i + 20, ""));
        lis.add(new Fila(80, i = i + 20, "______________"));
        lis.add(new Fila(80 + 90, i, "______________"));
        lis.add(new Fila(80, i = i + 20, "FIRMA CLIENTE", 9));
        lis.add(new Fila(80 + 90, i, "FIRMA CAJERO", 9));
        lis.add(new Fila(80, i = i + 20, "IMPRESO EL:" + currentTime, 8));
        lis.add(new Fila(80, i = i + 20, "Nota:Verificar su dinero antes de retirarse, ", 8));
        lis.add(new Fila(80, i = i + 20, "no se aceptan reclamos posteriores", 8));

        imprime(lis);
    }

    public static void imp_prove_mov(int id) {

        ProveMov rc = ProveMovData.getById(id);
        System.out.println("rc.getProve_nom=" + rc.getProve_nom());

        SimpleDateFormat sdf = new SimpleDateFormat(Conn.DEFAULT_DATE_STRING_FORMAT);
        SimpleDateFormat sdf2 = new SimpleDateFormat(Conn.DEFAULT_DATE_STRING_FORMAT_PE);

        Date dt = new Date();
        String currentTime = sdf.format(dt);

        List<Fila> lis = new CopyOnWriteArrayList<Fila>();
        int i = 20;
        lis.add(new Fila(80, i = i + 20, ""));
        lis.add(new Fila(80 + 20, i = i + 20, "M&G Negocios", 14, "Taahoma"));
        lis.add(new Fila(80, i = i + 20, "=== ADELANTOS/COBRO PROVE ==="));
        lis.add(new Fila(80, i = i + 20, "NUM:" + String.format("%04d", rc.getId()) + "  FECHA:" + sdf2.format(dt)));
        lis.add(new Fila(80, i = i + 10, ""));
        lis.add(new Fila(80, i = i + 20, "DE: " + rc.getProve_nom()));

        // lis.add(new Fila(80, i = i + 20, "GRAMOS: " + new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(rc.getCant_gr())));
        String moneda = "SIN MONEDA";
        String mov = "SIN MOV";
        double monto = 0;
        if (rc.getEsdolares() == 1) {
            moneda = "EN DÓLARES";
            if (rc.getEsadelanto() == 1) {
                mov = "ADELANTO";
                monto = rc.getAdelanto_do();
            } else {
                mov = "COBRO";
                monto = rc.getCobro_do();
            }
        }
        if (rc.getEsdolares() == 0) {
            moneda = " EN SOLES";
            if (rc.getEsadelanto() == 1) {
                mov = "ADELANTO";
                monto = rc.getAdelanto_so();
            } else {
                mov = "COBRO";
                monto = rc.getCobro_so();
            }
        }
        lis.add(new Fila(80, i = i + 20, "MONEDA: " + moneda));
        lis.add(new Fila(80, i = i + 20, "MOV.: " + mov));

        lis.add(new Fila(80, i = i + 20, "MONTO: " + new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(monto)));
        lis.add(new Fila(80, i = i + 20, ""));

        lis.add(new Fila(80, i = i + 20, ""));
        lis.add(new Fila(80, i = i + 20, "______________"));
        lis.add(new Fila(80 + 90, i, "______________"));
        lis.add(new Fila(80, i = i + 20, "FIRMA CLIENTE", 9));
        lis.add(new Fila(80 + 90, i, "FIRMA CAJERO", 9));
        lis.add(new Fila(80, i = i + 20, "IMPRESO EL:" + currentTime, 8));
        lis.add(new Fila(80, i = i + 20, "Nota:Verificar su dinero antes de retirarse, ", 8));
        lis.add(new Fila(80, i = i + 20, "no se aceptan reclamos posteriores", 8));

        imprime(lis);
    }

    public static void main(String[] args) {

        imp_compra(6);

    }
}

class Fila {

    int x;
    int y;
    String cadena;
    int tam;
    String fuente;//="Helvetica";

    public Fila() {

    }

    public Fila(int x, int y, String cadena) {
        this.x = x;
        this.y = y;
        this.cadena = cadena;
        this.tam = 10;
        this.fuente = "Helvetica";
    }

    public Fila(int x, int y, String cadena, int tam) {
        this.x = x;
        this.y = y;
        this.cadena = cadena;
        this.tam = tam;
        this.fuente = "Helvetica";
    }

    public Fila(int x, int y, String cadena, int tam, String fuente) {
        this.x = x;
        this.y = y;
        this.cadena = cadena;
        this.tam = tam;
        this.fuente = fuente;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public int getTam() {
        return tam;
    }

    public void setTam(int tam) {
        this.tam = tam;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

}
