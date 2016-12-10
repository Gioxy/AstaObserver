/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astaserver;

import java.util.Observable;

/**
 *
 * @author Paolo Galletta
 */
public class MonitorServ extends Observable
{
    private int baseAsta;
    private String nomeClient;

    public MonitorServ(int baseAsta) 
    {
        this.baseAsta = baseAsta;
        nomeClient="Server";
    }

    synchronized public int getBaseAsta() 
    {
        return baseAsta;
    }

    public String getNomeClient() 
    {
        return nomeClient;
    }

    public void setNomeClient(String nomeClient) 
    {
        this.nomeClient = nomeClient;
    }

    
    synchronized public void setBaseAsta(int baseAsta,String nomeCL) 
    {
        if(getBaseAsta()<baseAsta)
        {
            this.baseAsta = baseAsta;
            setNomeClient(nomeCL);
            setChanged();
            notifyObservers();
        }
    }
    
    
    
}
