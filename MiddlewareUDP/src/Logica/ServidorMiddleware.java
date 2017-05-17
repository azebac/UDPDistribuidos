package Logica;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Este es el main del servidor Middleware que se encargará de manejar la 
 * extensibilidad del proyecto
 * @author David
 */
public class ServidorMiddleware {
    
    /**
     *Lista la cual contiene todos los servidores disponibles en el middleware
     * No se esta manejando la desconexion de alguno de estos
     */
    private static ArrayList<Servidor> servidoresDisponibles = new ArrayList<Servidor>();
    //especificamos el tamaño del paquete
    private static int tamanoPaquete = 256;
    
    /**
     * Ejecución principal del servidor
     * @param argv 
     */
    public static void main(String argv[]) {
        //socket de comunicación
        DatagramSocket socket;
        boolean fin = false;

        //especificamos el puerto de escucha
        int puerto = 6001;
        
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
                String mensajeEntrante = "";
                mensajeEntrante = new String(mensajeEnBytes);
                String mensajeCliente = "";

                DatagramPacket paquete = new DatagramPacket(mensajeEnBytes, tamanoPaquete);
                DatagramPacket paqueteAEnviar = new DatagramPacket(mensajeEnBytes, tamanoPaquete);
                // Recibimos el paquete
                socket.receive(paquete);
                // Lo formateamos
                mensajeEntrante = new String(mensajeEnBytes).trim();
                // Lo mostramos por pantalla
                System.out.println(mensajeEntrante);
                //Obtenemos IP Y PUERTO del cliente
                puertoCliente = paquete.getPort();
                ipCliente = paquete.getAddress();
                //decodificamos el mensaje recibido
                String [] mensajeDecodificado = mensajeEntrante.split(";");
                System.out.println(mensajeDecodificado[0]);
                //Si el mensaje es un servidor que se quiere registrar
                if(mensajeDecodificado[0].equals("servidor")){
                    registrarNuevoServidor(ipCliente, puertoCliente,
                            mensajeDecodificado[1]);
                    //le respondo al servidor que ya esta registrado
                    mensajeCliente = "registrado";

                    //formateamos el mensaje de salida
                    mensajeEnBytesCliente = mensajeCliente.getBytes();

                    //Preparamos el paquete que queremos enviar
                    paqueteAEnviar = new DatagramPacket(mensajeEnBytesCliente, 
                            mensajeCliente.length(), ipCliente, puertoCliente);

                    // realizamos el envio
                    socket.send(paqueteAEnviar);
                }
                //Si el mensaje es un cliente que necesita recursos
                if (mensajeDecodificado[0].equals("recurso")) {
                    if (solicitarRecursosParaCliente(Integer.parseInt(mensajeDecodificado[1]),
                            mensajeDecodificado[2])) {
                        //le respondo al cliente que sus recursos fueron aprobados
                        mensajeCliente = "aprobado";

                        //formateamos el mensaje de salida
                        mensajeEnBytesCliente = mensajeCliente.getBytes();

                        //Preparamos el paquete que queremos enviar
                        paqueteAEnviar = new DatagramPacket(mensajeEnBytesCliente,
                                mensajeCliente.length(), ipCliente, puertoCliente);

                        // realizamos el envio
                        socket.send(paqueteAEnviar);
                    }
                }
                //Si el mensaje es un cliente que quiere la lista de recursos
                //disponibles
                if(mensajeDecodificado[0].equals("lista")){
                    //le respondo al cliente con la lista generada de servidores
                    mensajeCliente = enviarListaDeRecursosACliente();

                    //formateamos el mensaje de salida
                    mensajeEnBytesCliente = mensajeCliente.getBytes();

                    //Preparamos el paquete que queremos enviar
                    paqueteAEnviar = new DatagramPacket(mensajeEnBytesCliente,
                            mensajeCliente.length(), ipCliente, puertoCliente);

                    // realizamos el envio
                    socket.send(paqueteAEnviar);
                }
            } while (1 > 0);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
    }
  
    /**
     * Metodo que se encarga de registrar en el array un nuevo servidor de recursos
     * @param ip Ip del servidor a registrar
     * @param puerto Puerto del servidor a registrar
     * @param recurso Recurso que provee el servidor
     */
    private static void registrarNuevoServidor(InetAddress ip, int puerto, String recurso){
        Servidor nuevoServidor = new Servidor();
        nuevoServidor.setIp(ip);
        nuevoServidor.setPuerto(puerto);
        nuevoServidor.setRecurso(recurso);
        servidoresDisponibles.add(nuevoServidor);
    }
    
    /**
     * Esta funcion se encarga de solicitar al servidor correspondiente los recursos necesarios
     * solicitados por el cliente
     * @param cantidad Cantidad de recursos que solicita el cliente
     * @param recurso nombre del recurso que necesita el cliente
     * @return True si los recursos son aprobados, false en caso contrario
     */
    private static Boolean solicitarRecursosParaCliente(int cantidad, String recurso){
        //busco cual es el servidor en mi lista que dispone de dicho recurso
        Servidor servidorASolicitar = new Servidor();
        for (Servidor servidor : servidoresDisponibles) {
            if (servidor.getRecurso().equals(recurso)){
                servidorASolicitar = servidor;
            }
        }
        
        //creo un nuevo socket con sus respectivos elementos de comunicacion
        DatagramSocket socket;
        byte[] mensaje_bytes = new byte[tamanoPaquete];
        String mensaje = "";
        mensaje_bytes = mensaje.getBytes();
        
        //creo el paquete que se enviara al servidor con el recurso
        DatagramPacket paquete;
        String cadenaMensaje = "";
        DatagramPacket servPaquete;
        //defino el tamaño del buffer
        byte[] RecogerServidor_bytes = new byte[tamanoPaquete];

        try {
            socket = new DatagramSocket();
            //Obtengo la ip del servidor con el recurso
            InetAddress address = servidorASolicitar.getIp();
            //Convierto a string la cantidad de recursos que necesito
            mensaje = String.valueOf(cantidad);
            //obtengo la cantidad de bytes del mensaje
            mensaje_bytes = mensaje.getBytes();
            //lo meto en el paquete udp
            paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 6000);
            //lo envio
            socket.send(paquete);
            //Defino el tamaño de la respuesta que espero del servidor
            RecogerServidor_bytes = new byte[tamanoPaquete];
            //Esperamos a recibir un paquete
            servPaquete = new DatagramPacket(RecogerServidor_bytes, tamanoPaquete);
            socket.receive(servPaquete);
            //Convertimos el mensaje recibido en un string
            cadenaMensaje = new String(RecogerServidor_bytes).trim();
            //retorno true si el servidor aprobo los recursos
            return cadenaMensaje.equals("true");            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return false;
    }
    
    
    /**
     * Esta funcion se encarga de realizar un string formateado para enviarselo al cliente
     * simulando una lista que obtendra cuando le haga split al string
     * @return String que contiene todos los recursos disponibles en un formato especial
     */
    private static String enviarListaDeRecursosACliente(){
        //primero creo un string vacio
        String listaFormateada = "";
        //recorro toda mi lista de servidores y voy guardando sus recursos disponibles
        for (Servidor servidor : servidoresDisponibles) {
            listaFormateada = listaFormateada+servidor.getRecurso()+";";
        }
        //elimino el ultimo caracter que es un "|" que se agrega por la logica de la
        //concatenacion
        listaFormateada = listaFormateada.substring(0, listaFormateada.length()-1);
        //retorno la lista
        return listaFormateada;
    }
}
