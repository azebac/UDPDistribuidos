/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Recurso;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Este es el recurso que maneja este servidor
 * @author David
 */
public class Recurso {
    /**
     * Variable que contiene la cantidad de recursos del servidor
     */
    private static int Cantidad;
/**
 * Variable calendario utilizada para la obtencion de la hora
 */
    static Calendar calendario = new GregorianCalendar();
    /**
     * variables integer que contendran la hora, minuto y segundo para su
     * impresion
     */
    static int hora, minutos, segundos;
    
/**
 * Constructor base del recurso, por defecto un servidor no tiene recursos 
 * cuando inicia
 */
    public Recurso() {
        Recurso.Cantidad = 0;
    }
    
    /**
     * Funcion para obtener cuantos recursos tiene el servidor
     * @return Integer con la cantidad de recursos disponibles en el servidor
     */
    public static int getCantidad() {
        return Cantidad;
    }
    
    /**
     * Funcion para establecer la cantidad de recursos que tiene el servidor
     * @param cantidad la cantidad de recursos que se le pondran al servidor
     */
    public static void setCantidad(int cantidad) {
        Recurso.Cantidad = cantidad;
    }
    
    /**
     * Fucion que suma al stock
     * @param cantidad la cantidad que sera sumada al stock
     */
    public static void addCantidad(int cantidad){
        Recurso.Cantidad = Recurso.Cantidad + cantidad;
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        System.out.println("A la(s) "+hora+":"+minutos+": con "+segundos+""
                + "Se hizo un reestock");
        System.out.println("El stock actual es "+ Recurso.Cantidad);
    }
    
    /**
     * Funcion utilizada para sacar elementos del stock
     * @param cantidad cantidad a reducir de stock
     */
    public static void removeCantidad(int cantidad){
        Recurso.Cantidad = Recurso.Cantidad - cantidad;
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        System.out.println("A la(s) "+hora+":"+minutos+": con "+segundos+""
                + "Se saco del stock");
        System.out.println("El stock actual es "+ Recurso.Cantidad);
    }
}
