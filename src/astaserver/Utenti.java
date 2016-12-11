/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astaserver;

/**
 *
 * @author Gioele Salmaso
 */
public class Utenti 
{
    String nome;
    String indiIp;

    public Utenti(String nome, String indiIp) 
    {
        this.nome = nome;
        this.indiIp = indiIp;
    }

    @Override
    public String toString() {
        return "Utenti{" + "nome=" + nome + ", indiIp=" + indiIp + '}';
    }
    
    
    
    
}
