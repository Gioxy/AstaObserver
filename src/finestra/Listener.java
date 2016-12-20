package finestra;
/**
 * 
 * @author Gioele Salmaso
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import javax.swing.JOptionPane;

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
                /**procedura per la connessione
                 */
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
                    JOptionPane.showMessageDialog(finestra.getP(), "L'ip inserito non è valido");
                    finestra.getIp1().setEditable(true);
                    finestra.getAqIp().addActionListener(this);
                    finestra.getAqIp().setBackground(Color.RED);
                }
            }

        }
        //bottone per far entrare lo spettatore in modalità "spettatore" (nasconde i bottoni così che l'utente non possa più agire su di essi) 
        if (s.equalsIgnoreCase("nascondi")) {
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
                    /**controllo per vedere se l'offerta corrente è inferiore al saldo disponibile
                     */
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
