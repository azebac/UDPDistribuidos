/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Recurso.Recurso;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Este proceso controlará la produccion de productos y será apagado/encendido
 * por el monitor, es un hilo de la ejecución principal del productor
 * @author David
 */
public class ProcesoProductor  extends Thread {

    //Cantidad por la cual se hara restock
    private final int cantidadRestock = 5;
    //cantidad minima necesaria para hacer reestock;
    private final int cantidadMinimaRestock = 2;
    //El recurso que se consume
    private final Recurso recurso;
    //la cantidad de recursos que necesito
    private int recursosNecesarios = 0;
    


    /**
     * Constructor donde instancio el recurso del servidor
     * @param recurso Variable estatica donde el servidor almacena sus recursos
     */
    public ProcesoProductor(Recurso recurso){
        this.recurso = recurso;
    }
    
    @Override
    public void run() {
        //se programa un ciclo infinito para que este proceso siempre este 
        //intentando hacer restock
        while (1 > 0) {
            try {
                hacerRestock();
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcesoProductor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Metodo controlado por monitor que se encarga de realizar el restock
     */
    private synchronized void hacerRestock() throws InterruptedException{
        //Mientras me queden mas o igual que la cantidad minima o la
        int cantidadEnStock = Recurso.getCantidad();
        while (
                (Recurso.getCantidad() > recursosNecesarios) && (Recurso.getCantidad() > cantidadMinimaRestock)){
            //espero
            wait();
        }
        //Relleno el stock
        Recurso.addCantidad(cantidadRestock);
        
        //Notifico que ya hay stock
        notify();
    }
        
    /**
     * Funcion llamada por el servidor para reducir el stock
     * @param cantidad la cantidad que necesito
     * @throws InterruptedException ?
     * @return true cuando se completa la operacion de consumir
     */
    public synchronized Boolean consumirStock(int cantidad) throws InterruptedException{
        int cantidadEnStock = Recurso.getCantidad();
        //seteo cuantos recursos necesito
        recursosNecesarios = cantidad;
        //"revivo" el hilo productor    
        notify();
        while((Recurso.getCantidad() < cantidadMinimaRestock) || 
                (Recurso.getCantidad() < recursosNecesarios)){
            //mientras que el stock este por debajo del nivel requerido o minimo espero
            wait();
        }
        Recurso.removeCantidad(cantidad);
        return true;
            
    }
    
}
