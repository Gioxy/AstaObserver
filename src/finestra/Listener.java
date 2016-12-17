/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finestra;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author gioxy
 */
public class Listener implements ActionListener{
    Finestra finestra;
    public Listener(Finestra finestra){
        this.finestra = finestra;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        //bottone per joinare all'asta 
        if (s.equalsIgnoreCase("join")) {
            if (finestra.isExt() == false) {
                finestra.getAqIp().setBackground(Color.green);
                finestra.setIP(finestra.getIp1().getText());
                //procedura per la connessione
                try {
                    finestra.getIp1().setEditable(false);
                    finestra.getAqIp().removeActionListener(this);
                    finestra.setServ(new Socket(finestra.getIP(), finestra.NUMERO_PORTA));
                    finestra.setcA(new ClientAsta(finestra.getServ(),finestra));
                    finestra.getcA().start();
                } catch (NoRouteToHostException ex) {
                    JOptionPane.showMessageDialog(finestra.getP(), "L'ip inserito non è valido");
                    finestra.getIp1().setEditable(true);
                    finestra.getAqIp().addActionListener(this);
                    finestra.getAqIp().setBackground(Color.RED);
                } catch (IOException ex) {
                    //Logger.getLogger(Finestra.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(finestra.getP(), "L'ip inserito non è valido");
                    finestra.getIp1().setEditable(true);
                    finestra.getAqIp().addActionListener(this);
                    finestra.getAqIp().setBackground(Color.RED);
                }
            }

        }
        //bottone per far uscire l'utente (nasconde i bottoni così che l'utente non possa più agire su di essi) 
        if (s.equalsIgnoreCase("exit")) {
            finestra.setExt(true);
            finestra.getAqSaldo().setVisible(false);
            finestra.getRil().setVisible(false);
            finestra.getDisc().setVisible(false);
            finestra.getIp1().setEditable(false);
            finestra.getAqIp().removeActionListener(this);
        } else {
            //bottone per rilanciare 
            if (s.equalsIgnoreCase("rilancia") && finestra.isExt() == false) {
         
                if (Integer.parseInt(finestra.getAgs().getText()) >= Integer.parseInt(finestra.getRil().getText())) {
                    JOptionPane.showMessageDialog(finestra.getP(), "L'offera inserita risulta minore o uguale all'offerta corrente");
                } else {
                    //controllo per vedere se l'offerta corrente è inferiore al saldo disponibile
                    System.out.println("Devo rilanciare");
                    if (finestra.getRa() >= Integer.parseInt(finestra.getRil().getText())) {
                        finestra.setRa(finestra.getRa() - Integer.parseInt(finestra.getRil().getText()));
                        finestra.getSld().setText(String.valueOf(finestra.getRa()));
                        finestra.getcA().sendAsta(Integer.parseInt(finestra.getRil().getText()));
                    } else {
                        JOptionPane.showMessageDialog(finestra.getP(), "L'offerta che hai cercato di effettuare supera il tuo saldo disponibile");
                    }
                }

            }
        }
    }
}
