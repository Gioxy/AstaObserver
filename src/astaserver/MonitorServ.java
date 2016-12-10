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

    public MonitorServ(int baseAsta) 
    {
        this.baseAsta = baseAsta;
    }

    synchronized public int getBaseAsta() 
    {
        return baseAsta;
    }

    synchronized public void setBaseAsta(int baseAsta) 
    {
        if(getBaseAsta()<baseAsta)
        {
            this.baseAsta = baseAsta;
            setChanged();
            notifyObservers();
        }
    }
    
    
    
}
