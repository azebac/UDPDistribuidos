/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.net.DatagramSocket;

/**
 * Este hilo se encargara de mantener atendido a al cliente conectado
 * cada cliente conectado es un hilo diferente, asi permitimos la concurrencia
 * @author David
 */
public class ProcesoClienteConectado implements Runnable {
    
    //El socket por donde se conecto el cliente
    DatagramSocket socket;
    //El tamaño de los paquetes que se enviaran/recibiran
    int tamanoPaquete;
    
    /**
     * Metodo para instanciar las variables necesarias del hilo
     * @param socket El socket por donde llego el cliente
     * @param tamanoPaquete el tamaño de los paquetes UDP
     */
    public ProcesoClienteConectado(DatagramSocket socket, int tamanoPaquete){
        this.socket = socket;
        this.tamanoPaquete = tamanoPaquete;
    }

    @Override
    public void run() {
        
    }
    
}
