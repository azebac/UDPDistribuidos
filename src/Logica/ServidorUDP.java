/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

/**
 * Servidor UDP con monitor de recursos.
 * @author David
 */
import Recurso.Recurso;
import java.net.*;

public class ServidorUDP {

    //Instancio los recursos del sistema
    static Recurso recurso = new Recurso();

    public static void main(String argv[]) {

        DatagramSocket socket;
        boolean fin = false;
        //especificamos el tamaño del paquete
        int tamanoPaquete = 256;
        //especificamos el puerto de escucha
        int puerto = 6000;
        //Instancio el proceso productor
        ProcesoProductor productor = new ProcesoProductor(recurso);
        //Inicio el proceso productor
        productor.start();


        try {
            //Creamos el socket
            socket = new DatagramSocket(puerto);

            //variable donde guardare el puerto del cliente conectado
            int puertoCliente;
            //variable donde guardare la ip del cliente conectado
            InetAddress ipCliente;
            byte[] mensajeEnBytesCliente = new byte[tamanoPaquete];

            //Iniciamos el bucle de recibir peticiones del cliente
            do {
                byte[] mensajeEnBytes = new byte[tamanoPaquete];
                String mensaje = "";
                mensaje = new String(mensajeEnBytes);
                String mensajeCliente = "";

                DatagramPacket paquete = new DatagramPacket(mensajeEnBytes, tamanoPaquete);
                DatagramPacket paqueteAEnviar = new DatagramPacket(mensajeEnBytes, tamanoPaquete);
                // Recibimos el paquete
                socket.receive(paquete);
                // Lo formateamos
                mensaje = new String(mensajeEnBytes).trim();
                // Lo mostramos por pantalla
                System.out.println(mensaje);
                //Obtenemos IP Y PUERTO del cliente
                puertoCliente = paquete.getPort();
                ipCliente = paquete.getAddress();
                
                //Y notificamos al productor que consumiremos la cantidad solicitada
                productor.consumirStock(Integer.parseInt(mensaje));
                
                //le respondo al cliente true para simular que se envio el recurso
                mensajeCliente = "true";

                //formateamos el mensaje de salida
                mensajeEnBytesCliente = mensajeCliente.getBytes();

                //Preparamos el paquete que queremos enviar
                paqueteAEnviar = new DatagramPacket(mensajeEnBytesCliente, mensajeCliente.length(), ipCliente, puertoCliente);

                // realizamos el envio
                socket.send(paqueteAEnviar);

            } while (1 > 0);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
