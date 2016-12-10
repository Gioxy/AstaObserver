package astaserver;

/**
 * @author gioele.salmaso,matteo.scalone
 */
import static astaserver.AstaServer.baseAsta;
import static astaserver.AstaServer.i;
import static astaserver.AstaServer.n_utenti;
import static astaserver.AstaServer.utenti;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class AstaServer {

    final static int Numero_Porta = 3333;
    static Utenti[] utenti;
    static int n_utenti;//variabile che contiene il numero di partecipanti all'asta 
    static int i = 0;//indice per memorizzare ip e nome client
    static int baseAsta;//variabile che indica lo stato della miglior offerta 
    static MonitorServ ms;

    public static void main(String[] args) {
        Scanner ut = new Scanner(System.in);
        System.out.println("Quanti utenti possono partecipare all'asta?");
        n_utenti = ut.nextInt();
        utenti = new Utenti[n_utenti];//definisce la grandezza dell'array di utenti
        System.out.println(Arrays.deepToString(utenti));
        baseAsta = 100;
        ms = new MonitorServ(baseAsta);
        try {
            ServerSocket server = new ServerSocket(Numero_Porta, n_utenti);
            while (true) {
                Socket client = server.accept();
                Cliente clt = new Cliente(client, ms);
                ms.addObserver(clt);
                System.out.println(ms.countObservers());
                clt.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

class Cliente extends Thread implements Observer {

    Socket client;
    BufferedReader in;
    PrintWriter out;
    MonitorServ ms;

    public Cliente(Socket client, MonitorServ ms) {
        this.client = client;
        this.ms = ms;
        try{
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));//
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            String nome = in.readLine();//stringa che memorizza il nome del client
            System.out.println("Si è collegato l'utente con ip " + client.getInetAddress() + " con il nome " + nome);

            //memorizzazione ip e nome del client all'interno del array di utenti
            utenti[i] = new Utenti(nome, client.getInetAddress().toString());
            i++;
            System.out.println(Arrays.deepToString(utenti));
            //comunicazione base d'asta ai client
            String risposta = String.valueOf(baseAsta);
            out.println(risposta);

            //lettura rilancio da parte del client e set del prezzo dell'asta
            String lettRil = in.readLine();
            System.out.println("Ricevuto: " + lettRil + " da " + utenti[i - 1].nome);
            ms.setBaseAsta(Integer.parseInt(lettRil));
            System.out.println("Current asta: " + ms.getBaseAsta());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object o1) {
        System.out.println("Aggiorno");
        System.out.println("asta: " + ms.getBaseAsta());
        out.println(String.valueOf(ms.getBaseAsta()));
    }
}
