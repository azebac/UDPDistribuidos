package Logica;

import java.net.InetAddress;

/**
 * Esta clase se encargará de guardar los datos de un servidor que esté disponible
 * en el middleware
 * @author David
 */
public class Servidor {
    
    /**
     * ip del servidor
     */
    private InetAddress ip;
    
    /**
     * Puerto del servidor
     */
    private int puerto;
    
    /**
     * recurso que posee el servidor
     */
    private String recurso;
    
    /**
     * Metodo de instanciacion basico del servidor
     */
    public Servidor(){}

    /**
     * Metodo para obtener la ip
     * @return string con la ip
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * Metodo para setear la ip
     * @param ip del servidor
     */
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }
    
    /**
     * metodo para obtener el puerto del servidor
     * @return int con el puerto asociado al servidor
     */
    public int getPuerto() {
        return puerto;
    }

    /**
     * Metodo para setear el puerto del servidor
     * @param puerto int conteniendo el puerto
     */
    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
    
    /**
     * Metodo para obtener el nombre del recurso disponible en el servidor
     * @return String que contiene el recurso que tiene disponible el servidor
     */
    public String getRecurso() {
        return recurso;
    }

    /**
     * Metodo utilizado para establecer el recurso que provee el servidor
     * @param recurso string que contiene el nombre del recurso
     */
    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }
    
    
}
