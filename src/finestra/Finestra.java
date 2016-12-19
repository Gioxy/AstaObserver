package finestra;
/**
 * 
 * @author Gioele Salmaso
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.net.*;

public class Finestra {

    final int NUMERO_PORTA = 3333;//porta alla quale il client si connetterà al server 
    Socket serv;

    JTextField sld;//textefield con il saldo disponibile 
    JPanel p = new JPanel();
    JButton AqIp = new JButton("join");
    String ipHint = "inserisci ip ";
    String rilHint = "rilancia";
    JTextField ip1 = new JTextField(ipHint);
    String IP;
    JButton AqSaldo = new JButton("rilancia");//bottone per rilanciare 
    JButton disc = new JButton("Nascondi");
    JTextField ril = new JTextField(rilHint);
    JTextField ags;//TextField per l'offerta corrente
    JTextField sag;//TextField per chi ha effettuato l'offerta corrente
    int ra;//random per il saldo 
    String nome;//nome inserito dall'utente
    ClientAsta cA;//Thread che gestisce gli in ed out del client 
    JLabel tempo;//label per visualizzare il tempo 
    JLabel tx;
    boolean ext;
    JFrame f;
    public Finestra(String nome) {
        this.nome = nome;
        ext = false;
        f = new JFrame("ASTA");
        f.setSize(400, 400);
        f.setLocation(500, 100);
        f.setVisible(true);
        f.setLayout(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        p.setSize(400, 400);
        p.setLayout(null);
        p.setVisible(true);
        p.setBackground(Color.GRAY);
        f.add(p);
        
        tx=new JLabel("Scade fra:");
        tx.setSize(100, 100);
        tx.setLocation(20, 85);
        tx.setVisible(true);
        p.add(tx);
        
        tempo = new JLabel("");
        tempo.setSize(40,30);
        tempo.setLocation(40, 140);
        tempo.setVisible(true);
        tempo.setForeground(Color.GREEN);
        tempo.setFont(new Font("Serif",Font.PLAIN,15));
        p.add(tempo);
        //FocusListener che cancella/rimette il testo del textfield del ip appena l'utente schiccia su quest'ultimo 
        FocusListener ipvisibile = new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals(ipHint)) {
                    tf.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals("")) {
                    tf.setText(ipHint);
                }
            }

        };
        //FocusListener che cancella/rimette il testo del textfield del rilancio appena l'utente schiccia su quest'ultimo 
        FocusListener rilvisibile = new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals(rilHint)) {
                    tf.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                JTextField tf = (JTextField) fe.getSource();
                if (tf.getText().equals("")) {
                    tf.setText(rilHint);
                }
            }

        };
        Listener l=new Listener(this);
        
        ip1.setSize(100, 30);                                    //casella per l' ip
        ip1.setLocation(30, 30);
        ip1.addFocusListener(ipvisibile);
        ip1.setVisible(true);
        p.add(ip1);

        ril.setSize(100, 30);                                    //casella per rilancio
        ril.setLocation(30, 300);
        ril.addFocusListener(rilvisibile);
        ril.setVisible(true);
        p.add(ril);

        AqSaldo.setSize(100, 30);                                //bottone per rilanciare
        AqSaldo.setLocation(150, 300);
        AqSaldo.addActionListener(l);
        AqSaldo.setVisible(true);
        AqSaldo.setBackground(Color.LIGHT_GRAY);
        p.add(AqSaldo);

        disc.setSize(100, 30);                                   //bottone per disconnettersi (exit)
        disc.setLocation(250, 300);
        disc.addActionListener(l);
        disc.setVisible(true);
        disc.setBackground(Color.LIGHT_GRAY);
        p.add(disc);

        AqIp.setSize(70, 30);                                    //bottone per acquisizione ip (join)
        AqIp.setLocation(150, 30);
        AqIp.setVisible(true);
        AqIp.setBackground(Color.red);
        AqIp.addActionListener(l);
        p.add(AqIp);

        Random random = new Random();
        ra = random.nextInt(5000);
        String SaldoStr = String.valueOf(ra);

        JLabel SaldoDisponibile = new JLabel("Saldo Disponibile");
        SaldoDisponibile.setSize(150, 30);
        SaldoDisponibile.setLocation(150, 70);
        SaldoDisponibile.setVisible(true);
        SaldoDisponibile.setForeground(Color.WHITE);
        p.add(SaldoDisponibile);

        sld = new JTextField(SaldoStr);                  //mostro il saldo disponibile
        sld.setSize(100, 30);
        sld.setLocation(30, 70);
        sld.setVisible(true);
        sld.setEditable(false);
        p.add(sld);

        //label per "titolo" offerta corrente 
        JLabel offerta = new JLabel("OFFERTA CORRENTE");
        offerta.setSize(150, 30);
        offerta.setLocation(130, 120);
        offerta.setVisible(true);
        offerta.setForeground(Color.BLACK);
        p.add(offerta);
        //label per offerta corrente
        JLabel soldi = new JLabel("€");
        soldi.setSize(150, 30);
        soldi.setLocation(245, 170);
        soldi.setVisible(true);
        soldi.setForeground(Color.WHITE);
        p.add(soldi);
        //textfield per offerta corrente 
        ags = new JTextField();
        ags.setSize(100, 30);
        ags.setLocation(200, 200);
        ags.setVisible(true);
        ags.setEditable(false);
        p.add(ags);
        //label per nome cliente 
        JLabel cl = new JLabel("Nome cliente");
        cl.setSize(150, 30);
        cl.setLocation(100, 170);
        cl.setVisible(true);
        cl.setForeground(Color.WHITE);
        p.add(cl);
        //textfield per nome del cliente con l'offerta maggiore
        sag = new JTextField();
        sag.setSize(100, 30);
        sag.setLocation(100, 200);
        sag.setVisible(true);
        sag.setEditable(false);
        p.add(sag);
    }


    //inizio metodi getter and setter per i vari campi 
    public Socket getServ() {
        return serv;
    }

    public void setServ(Socket serv) {
        this.serv = serv;
    }

    public JTextField getSld() {
        return sld;
    }

    public void setSld(JTextField sld) {
        this.sld = sld;
    }

    public JPanel getP() {
        return p;
    }

    public void setP(JPanel p) {
        this.p = p;
    }

    public JButton getAqIp() {
        return AqIp;
    }

    public void setAqIp(JButton AqIp) {
        this.AqIp = AqIp;
    }

    public String getIpHint() {
        return ipHint;
    }

    public void setIpHint(String ipHint) {
        this.ipHint = ipHint;
    }

    public String getRilHint() {
        return rilHint;
    }

    public void setRilHint(String rilHint) {
        this.rilHint = rilHint;
    }

    public JTextField getIp1() {
        return ip1;
    }

    public void setIp1(JTextField ip1) {
        this.ip1 = ip1;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public JButton getAqSaldo() {
        return AqSaldo;
    }

    public void setAqSaldo(JButton AqSaldo) {
        this.AqSaldo = AqSaldo;
    }

    public JButton getDisc() {
        return disc;
    }

    public void setDisc(JButton disc) {
        this.disc = disc;
    }

    public JTextField getRil() {
        return ril;
    } 

    public void setRil(JTextField ril) {
        this.ril = ril;
    }

    public JTextField getAgs() {
        return ags;
    }

    public void setAgs(JTextField ags) {
        this.ags = ags;
    }

    public JTextField getSag() {
        return sag;
    }

    public void setSag(JTextField sag) {
        this.sag = sag;
    }

    public int getRa() {
        return ra;
    }

    public void setRa(int ra) {
        this.ra = ra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ClientAsta getcA() {
        return cA;
    }

    public void setcA(ClientAsta cA) {
        this.cA = cA;
    }

    public JLabel getTempo() {
        return tempo;
    }

    public void setTempo(JLabel tempo) {
        this.tempo = tempo;
    }

    public boolean isExt() {
        return ext;
    }

    public void setExt(boolean ext) {
        this.ext = ext;
    }
    
    

    public static void main(String[] args) {
        String nome = JOptionPane.showInputDialog("Inserisci il tuo nickname");
        Finestra finestra = new Finestra(nome);
    }

}

