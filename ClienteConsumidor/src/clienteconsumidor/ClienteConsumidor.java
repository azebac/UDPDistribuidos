/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienteconsumidor;

/**
 *
 * @author karliana
 */
public class ClienteConsumidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        InterfazCliente ventana= new InterfazCliente(); 
        ventana.setVisible(true);
        ventana.setAlwaysOnTop(true);
        
    }
    
}
