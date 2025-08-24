/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv=new HostBlackListsValidator();
        long inicio = System.nanoTime();
        int hilos = 100;
        List<Integer> blackListOcurrences=hblv.checkHost("202.24.34.55", hilos);
        long fin = System.nanoTime();
        double tiempoSegundos = (fin - inicio) / 1_000_000_000.0;
        System.out.println("Tiempo de ejecución: " + tiempoSegundos + " segundos para "+hilos+" hilos");
        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
        
    }
    
}
