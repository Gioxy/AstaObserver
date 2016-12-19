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
