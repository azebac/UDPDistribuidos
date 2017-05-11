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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ServidorUDP {

    //Instancio los recursos del sistema
    static Recurso recurso = new Recurso();
    //especificamos el tamaño del paquete
    static int tamanoPaquete = 256;

    public static void main(String argv[]) {
        
        //me registro en el middleware antes que nada
        registrarEnMiddleWare();
        DatagramSocket socket;
        boolean fin = false;

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
    
    /**
     * Esta funcion se encarga de registrar este servidor en el middleware
     * para notificar que recursos tiene disponibles
     */
    private static void registrarEnMiddleWare() {
        Boolean registroExitoso = false;
        String ip = "0.0.0.0";
        String nombreRecurso = "";
        while (!registroExitoso) {
            try {
                //pido la ip del middleware
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Escriba la ip del servidor middleware");
                ip = in.readLine();
                InetAddress ipMiddleware = InetAddress.getByName(ip);
                //pido el nombre del recurso que tiene este servidor
                System.out.println("Escriba el nombre del recurso que posee");
                nombreRecurso = in.readLine();
                
                //seteo un socket udp y defino el string a enviar
                DatagramSocket socket;
                byte[] mensaje_bytes = new byte[tamanoPaquete];
                String mensaje = "servidor;"+nombreRecurso;
                mensaje_bytes = mensaje.getBytes();
                
                //defino el paquete Paquete
                DatagramPacket paquete;
                //creo un string para guardar la respuesta del servidor
                String respuestaServidorMiddleware = "";
                //creo un placeholder para la respuesta del paquete del servidor
                DatagramPacket servPaquete;
                //defino el tamaño de la respuesta del servidor
                byte[] RecogerServidor_bytes = new byte[tamanoPaquete];
                //instancio el socket
                socket = new DatagramSocket();
                //convierto en bytes el mensaje a enviar
                mensaje_bytes = mensaje.getBytes();
                //lo meto en el paquete y lo envio a la ip a traves del puerto 6001
                paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), ipMiddleware, 6001);
                socket.send(paquete);

                RecogerServidor_bytes = new byte[tamanoPaquete];

                //Esperamos a recibir un paquete
                servPaquete = new DatagramPacket(RecogerServidor_bytes, tamanoPaquete);
                socket.receive(servPaquete);

                //Convertimos el mensaje recibido en un string
                respuestaServidorMiddleware = new String(RecogerServidor_bytes).trim();
                //pregunto si el middleware me acepto
                if(respuestaServidorMiddleware.equals("registrado"));
                {
                    //notifico que fue aceptado
                    System.out.println("Registrado en el middleware");
                    //rompo el ciclo infinito
                    registroExitoso=true;
                }
                
            } catch (IOException e) {
                System.out.println("Error en la lectura de datos");
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
    }
}
