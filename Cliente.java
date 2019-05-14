import java.time.LocalDate;
import java.util.*;

public class Cliente extends Ator{
    private Ponto posicaoI;
    private Ponto posicaoF;
    private Set<Aluguer> historico;

    // -> aqui da jeito ter 4 construtores i think
    public Cliente() {
        super();
        this.posicaoI = new Ponto();
        this.posicaoF = new Ponto();
        this.historico = new TreeSet<>();
    }

    public Cliente(String email, String name, String password, String address, LocalDate date, Ponto posicaoI, Ponto posicaoF, Set<Aluguer> historico) {
        super(email, name, password, address, date);
        this.posicaoI = posicaoI;
        this.posicaoF = posicaoF;
        setHistorico(historico);
    }

    public Cliente(Ator at, Ponto posicaoI, Ponto posicaoF, Set<Aluguer> historico) {
        super(at);
        this.posicaoI = posicaoI.clone();
        this.posicaoF = posicaoF.clone();
        setHistorico(historico);
    }

    public Cliente(Cliente cl) {
        super(cl.getEmail(),cl.getName(),cl.getPassword(),cl.getAddress(),cl.getBirthday());
        this.posicaoI = cl.getPosicaoI();
        this.posicaoF = cl.getPosicaoF();
        setHistorico(cl.getHistorico());

    }

    public Ponto getPosicaoI() {
        return this.posicaoI.clone();
    }

    public Ponto getPosicaoF() {return this.posicaoF.clone();}

    public Set<Aluguer> getHistorico() {
        Set<Aluguer> getter = new TreeSet<>();
        Iterator<Aluguer> it = this.historico.iterator();
        while(it.hasNext()) {
            getter.add(it.next().clone());
        }
        return getter;
    }

    public void setPosicaoI(Ponto posicao) {
        this.posicaoI = posicao.clone();
    }

    public void setPosicaoF(Ponto posicao) {this.posicaoF = posicao.clone();}

    public void setHistorico(Set<Aluguer> al) {
        this.historico = new TreeSet<>();
        Iterator<Aluguer> it = al.iterator();
        while(it.hasNext()) {
            this.historico.add(it.next().clone());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString()).
        append("\nAlugueres realizados: \n").append(this.historico).
        append("\nPosição atual: (x , y) = ").append(this.posicaoI.toString()).
        append("Posição pretendida: (x , y) = ").append(this.posicaoF.toString()).append("\n");

        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return (super.equals(cliente) &&
                this.historico.equals(cliente.getHistorico()) &&
                this.posicaoI.equals(cliente.getPosicaoI()) &&
                this.posicaoF.equals(cliente.getPosicaoF()));
    }

    public Cliente clone(){
        return new Cliente(this);
    }




    public Veiculo getCloser (List<Veiculo> lv){
        /*
        Falta try catch para listas vazias
        */
        double x = lv.get(0).getPosicao().distancia(this.posicaoI);
        Veiculo veiculo = lv.get(0).clone();
        for(Veiculo v:lv){
            if(this.posicaoI.distancia(v.getPosicao()) < x){
                x = this.posicaoI.distancia(v.getPosicao());
                veiculo = v.clone();
            }
        }
        return veiculo;
    }

    public Veiculo getCheaper (List<Veiculo> lv){
        /*
        Falta try catch para listas vazias
        */
        double dist = this.getPosicaoI().distancia(this.posicaoF);
        double preco = lv.get(0).getPriceKm() * dist;
        Veiculo veiculo = lv.get(0).clone();

        for (Veiculo v : lv){
            if((dist * v.getPriceKm()) < preco){
                preco = dist * v.getPriceKm();
                veiculo = v.clone();
            }
        }

        return veiculo;
    }

    public Veiculo getCloserDist (List<Veiculo> lv, double d){

        /*
        ESTA PRECISA MESMO DE TRY CATCH LOL
        */
        double dist = this.getPosicaoI().distancia(this.posicaoF);
        double preco = -1;
        Veiculo veiculo;
        int i = 0;

        for (Veiculo v : lv){
            if(((dist * v.getPriceKm()) < preco || preco == -1)  &&  dist <= d){
                preco = dist * v.getPriceKm();
                veiculo = v.clone();
                i++;

            }
        }
        //if (i>0) return veiculo;
        return new Gasolina();
    }

    public boolean getVeiculoSpec (List<Veiculo> lv, Veiculo v){
        return lv.contains(v);
    }

    public Veiculo getBestAutonomy (List<Veiculo> lv, double autonomia){
        Veiculo veiculo = lv.get(0);
        for (Veiculo v : lv){
            if (v.getAutonomia() == autonomia) {
                veiculo = v;
                break;
            }
        }
        return veiculo;
    }

}
