/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

/**
 *
 * @author David
 */
import java.net.*;
import java.io.*;

public class servidorudp {

    public static void main(String argv[]) {

        DatagramSocket socket;
        boolean fin = false;
        //especificamos el tamaño del paquete
        int tamano_paquete = 256;
        //especificamos el puerto de escucha
        int puerto = 6000;

        try {
            //Creamos el socket
            socket = new DatagramSocket(puerto);
            byte[] mensaje_bytes = new byte[tamano_paquete];
            String mensaje = "";
            mensaje = new String(mensaje_bytes);
            String mensajeComp = "";

            DatagramPacket paquete = new DatagramPacket(mensaje_bytes, tamano_paquete);
            DatagramPacket envpaquete = new DatagramPacket(mensaje_bytes, tamano_paquete);

            int puertoCliente;
            InetAddress ipCliente;
            byte[] mensaje2_bytes = new byte[tamano_paquete];

            //Iniciamos el bucle
            do {
                // Recibimos el paquete
                socket.receive(paquete);
                // Lo formateamos
                mensaje = new String(mensaje_bytes).trim();
                // Lo mostramos por pantalla
                System.out.println(mensaje);
                //Obtenemos IP Y PUERTO del cliente
                puertoCliente = paquete.getPort();
                ipCliente = paquete.getAddress();

                if (mensaje.startsWith("fin")) {
                    mensajeComp = "chauuuuuuu cliente";
                }

                if (mensaje.startsWith("hola")) {
                    mensajeComp = "hola cliente";
                }

                //formateamos el mensaje de salida
                mensaje2_bytes = mensajeComp.getBytes();

                //Preparamos el paquete que queremos enviar
                envpaquete = new DatagramPacket(mensaje2_bytes, mensajeComp.length(), ipCliente, puertoCliente);

                // realizamos el envio
                socket.send(envpaquete);

            } while (1 > 0);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
