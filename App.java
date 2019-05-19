import java.time.LocalDate;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class App {
    private MyLog logNegocio = new MyLog();
    private Menu menuCliente,menuProprietario,menuSign;
    private MenuLogin menuLogin;

    public static void main(String[] args){
        new App().run();
    }

    private void run2(){
        List<Veiculo> ve = this.logNegocio.getCarros().values().stream().collect(Collectors.toList());
        System.out.println(ve);
    }
    private App(){
        String[] cliente = {"Alugar carro mais perto",
                "Alugar o carro mais barato",
                "Alugar o carro mais barato num raio",
                "Alugar um carro em específico",
                "Alterar a minha localização",
                "Alterar o meu destino",
                "Ver o meu histórico de alugueres aceites",
                "Ver a minha localização atual",
                "Ver o meu destino atual",
                "Ver minha informação pessoal"};
        String[] props = {"Abastacer um dos meus veículos",
                "Alterar preço/km de um dos meus veículos",
                "Aceitar/Rejeitar um aluguer",
                "Inserir nova viatura para aluguer",
                "Remover uma viatura de aluguer",
                "Ver o meu histórico de alugueres aceites",
                "Ver a minha frota atual de veículos",
                "Ver a minha informação pessoal"};

        String[] sign = {"Sign-in",
                        "Sign-up"};

        String[] login = {"Cliente", "Proprietário"};
        this.menuSign = new Menu(sign);
        this.menuLogin = new MenuLogin(login);
        this.menuCliente = new Menu(cliente);
        this.menuProprietario = new Menu(props);
    }

    private void run(){
        /*
        do{
           if(!this.menuLogin.executaLogin()) break;
           System.out.println("opcao " + this.menuLogin.getTipo());
        } while(!this.logNegocio.verificaLogin(this.menuLogin.getTipo(),this.menuLogin.getPassword(),this.menuLogin.getEmail()));
        */
        do {
            this.menuSign.executa();
            switch(menuSign.getOp()) {
                case 1:
                    do {
                        this.menuLogin.executaReader();
                        if (this.menuLogin.getOp() == 0) {
                            break;
                        }
                        do {
                            this.menuLogin.executaParametros();
                        } while (!this.logNegocio.verificaLogin(this.menuLogin.getOp(), this.menuLogin.getPassword(), this.menuLogin.getEmail()));

                        switch (menuLogin.getOp()) {
                            case 1:
                                do {
                                    this.menuCliente.executa();
                                    switch (menuCliente.getOp()) {
                                        case 1:
                                            String tipo;
                                            int rateCheap;
                                            System.out.println("Inserir o tipo de Carro: Gasolina | Hibrido | Electrico");
                                            do{
                                                System.out.print("Tipo: ");
                                                tipo = menuLogin.lerTipo();
                                            }while (tipo==null);
                                            System.out.println("A minha posição atual é: " + this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoI());
                                            System.out.println("Alugado o carro mais perto:\n");
                                            Veiculo cheapest = this.logNegocio.rentClosest(menuLogin.getPassword(),tipo);
                                            System.out.println(cheapest!=null?cheapest:"Não existe nenhum carro com estas condições.");
                                            if(cheapest!=null) {
                                                Aluguer theCheap = new Aluguer();
                                                theCheap.setAluguerID(this.logNegocio.getCounter());
                                                this.logNegocio.updateCounter();
                                                theCheap.setVeiculoID(cheapest.getID());
                                                theCheap.setClienteID(this.menuLogin.getPassword());
                                                theCheap.setPropID(cheapest.getProp());
                                                theCheap.setTipo("MaisPerto");
                                                theCheap.setTipoVeiculo(cheapest.getTipo());
                                                theCheap.setInicioPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoI());
                                                theCheap.setFimPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoF());
                                                theCheap.setPosInicialVeiculo(cheapest.getPosicao());
                                                //this.logNegocio.addAluguer(this.menuLogin.getPassword(),theCheap,cheapest);
                                                do{
                                                    System.out.print("Classificação do proprietário (0-100): ");
                                                    rateCheap=this.menuLogin.leInt();
                                                }while(rateCheap==-1);
                                                this.logNegocio.addClassificacao(cheapest.getProp(),rateCheap,"Prop");
                                                do{
                                                    System.out.print("Classificação do veículo (0-100): ");
                                                    rateCheap=this.menuLogin.leInt();
                                                }while(rateCheap==-1);
                                                this.logNegocio.addClassificacao(cheapest.getID(),rateCheap,"Veiculo");
                                                this.logNegocio.addAluguerQueue(theCheap);
                                            }
                                            break;
                                        case 2:
                                            String tipoC;
                                            int rateCheap2;
                                            System.out.println("Inserir o tipo de Carro: Gasolina | Hibrido | Electrico");
                                            do{
                                                System.out.print("Tipo: ");
                                                tipoC = menuLogin.lerTipo();
                                            }while (tipoC==null);
                                            Veiculo cheap = logNegocio.rentCheapest(menuLogin.getPassword(),tipoC);
                                            System.out.println("O meu destino é: "+ this.logNegocio.getCliente(this.menuLogin.getPassword()).getPosicaoF());
                                            System.out.println("Alugado o carro seguinte, com custo de " + cheap.calculaTarifa(this.logNegocio.getClienteCoordF(this.menuLogin.getPassword()),cheap.getPosicao()) +" €.\n");
                                            System.out.println(cheap!=null?cheap:"Não existe nenhum carro com estas condições.");
                                            if(cheap!=null) {
                                                Aluguer theCheap2 = new Aluguer();
                                                theCheap2.setAluguerID(this.logNegocio.getCounter());
                                                this.logNegocio.updateCounter();
                                                theCheap2.setVeiculoID(cheap.getID());
                                                theCheap2.setClienteID(this.menuLogin.getPassword());
                                                theCheap2.setPropID(cheap.getProp());
                                                theCheap2.setTipo("MaisBarato");
                                                theCheap2.setTipoVeiculo(cheap.getTipo());
                                                theCheap2.setInicioPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoI());
                                                theCheap2.setFimPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoF());
                                                theCheap2.setPosInicialVeiculo(cheap.getPosicao());
                                                do{
                                                    System.out.print("Classificação do proprietário (0-100): ");
                                                    rateCheap2=this.menuLogin.leInt();
                                                }while(rateCheap2==-1);
                                                this.logNegocio.addClassificacao(cheap.getProp(),rateCheap2,"Prop");
                                                do{
                                                    System.out.print("Classificação do carro (0-100): ");
                                                    rateCheap2=this.menuLogin.leInt();
                                                }while(rateCheap2==-1);
                                                this.logNegocio.addClassificacao(cheap.getID(),rateCheap2,"Veiculo");
                                               // this.logNegocio.addAluguer(this.menuLogin.getPassword(),theCheap2,cheap);
                                                this.logNegocio.addAluguerQueue(theCheap2);
                                            }
                                            break;
                                        case 3:
                                            System.out.println("Inserir raio máximo até ao carro:\n");
                                            double d;
                                            int rate3;
                                            do{
                                                System.out.print("Raio = ");
                                                d=menuLogin.lerDouble();
                                            }while(d==-1);
                                            System.out.println("A minha posição atual é: " + this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoI());
                                            Veiculo raioCheap =logNegocio.rentCheapest(menuLogin.getPassword(),d);
                                            if(raioCheap!=null) {
                                                double disttotal = raioCheap.getPosicao().distancia(this.logNegocio.getClienteCoordI(this.menuLogin.getPassword()));
                                                System.out.println("Alugado o veículo mais barato, com distância de " + (double) Math.round(disttotal*100)/100 + "m.\n");
                                                System.out.println(raioCheap);
                                                Aluguer theCheap3 = new Aluguer();
                                                theCheap3.setAluguerID(this.logNegocio.getCounter());
                                                this.logNegocio.updateCounter();
                                                theCheap3.setVeiculoID(raioCheap.getID());
                                                theCheap3.setClienteID(this.menuLogin.getPassword());
                                                theCheap3.setPropID(raioCheap.getProp());
                                                theCheap3.setTipo("MaisBarato");
                                                theCheap3.setTipoVeiculo(raioCheap.getTipo());
                                                theCheap3.setInicioPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoI());
                                                theCheap3.setFimPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoF());
                                                theCheap3.setPosInicialVeiculo(raioCheap.getPosicao());
                                                do{
                                                    System.out.print("Classificação do proprietário (0-100): ");
                                                    rate3=this.menuLogin.leInt();
                                                }while(rate3==-1);
                                                this.logNegocio.addClassificacao(raioCheap.getProp(),rate3,"Prop");

                                                do{
                                                    System.out.print("Classificação do carro (0-100): ");
                                                    rate3=this.menuLogin.leInt();
                                                }while(rate3==-1);
                                                this.logNegocio.addClassificacao(raioCheap.getID(),rate3,"Veiculo");
                                                //this.logNegocio.addAluguer(this.menuLogin.getPassword(),theCheap3,raioCheap);
                                                this.logNegocio.addAluguerQueue(theCheap3);
                                            }
                                            else {
                                                System.out.println("Não há nenhum veículo nesse raio.");
                                            }
                                            break;
                                        case 4:
                                            double d2;
                                            String lido;
                                            int rate4;
                                            System.out.println("Aqui estão os carros todos que existem num raio:");
                                            do{
                                                System.out.print("Raio = ");
                                                d2 = menuLogin.lerDouble();
                                            }while (d2==-1);
                                            try {
                                                List<Veiculo> ret = availableCars(logNegocio.getCliente(menuLogin.getPassword()),logNegocio.getCarros(),d2);
                                                System.out.println(ret);
                                                System.out.println("Insira a matrícula do carro que deseja alugar: ");
                                                do {
                                                    System.out.print("Matrícula: ");
                                                    lido = menuLogin.leString();
                                                }while(lido==null);
                                                System.out.println();
                                                try {
                                                    Veiculo query4 = rentID(ret,lido);
                                                    System.out.println("Dentre a lista de carros foi alugado:\n");
                                                    System.out.println(query4);
                                                    Aluguer theCheap4 = new Aluguer();
                                                    theCheap4.setAluguerID(this.logNegocio.getCounter());
                                                    this.logNegocio.updateCounter();
                                                    theCheap4.setVeiculoID(query4.getID());
                                                    theCheap4.setClienteID(this.menuLogin.getPassword());
                                                    theCheap4.setPropID(query4.getProp());
                                                    theCheap4.setTipo("MaisBarato");
                                                    theCheap4.setTipoVeiculo(query4.getTipo());
                                                    theCheap4.setInicioPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoI());
                                                    theCheap4.setFimPercurso(this.logNegocio.getCliente(menuLogin.getPassword()).getPosicaoF());
                                                    theCheap4.setPosInicialVeiculo(query4.getPosicao());
                                                    do{
                                                        System.out.print("Classificação do proprietário (0-100): ");
                                                        rate4=this.menuLogin.leInt();
                                                    }while(rate4==-1);
                                                    this.logNegocio.addClassificacao(query4.getProp(),rate4,"Prop");

                                                    do{
                                                        System.out.print("Classificação do carro (0-100): ");
                                                        rate4=this.menuLogin.leInt();
                                                    }while(rate4==-1);
                                                    this.logNegocio.addClassificacao(query4.getID(),rate4,"Veiculo");
                                                    //this.logNegocio.addAluguer(this.menuLogin.getPassword(),theCheap4,query4);
                                                    this.logNegocio.addAluguerQueue(theCheap4);
                                                }
                                                catch (PrintError e){
                                                    System.out.println(e.getMessage());
                                                }
                                            }
                                            catch (PrintError e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                            /*
                                        case 5:
                                            System.out.println("Inserir autonomia:\n");
                                            double aut;
                                            do{
                                                System.out.print("Autonomia = ");
                                                aut=menuLogin.lerDouble();
                                            }while(aut==-1);
                                           // try{
                                               // System.out.println(rentAutonomy(logNegocio.getCliente(menuLogin.getPassword()),logNegocio.getCarros(),aut));
                                           // }
                                           // catch (PrintError e){
                                             //   System.out.println(e.getMessage());
                                           // }
                                            break;
                                            */
                                        case 5:
                                            System.out.println("A localização atual é: " + logNegocio.getClienteCoordI(menuLogin.getPassword()));
                                            System.out.println("Inserir novas coordenadas: ");
                                            Ponto novoPonto;
                                            do {
                                                novoPonto=menuLogin.lerCoordenada();
                                            } while(novoPonto==null);
                                            logNegocio.setClientCoordI(menuLogin.getPassword(),novoPonto);
                                            System.out.println("Ok. Localização atual: " + novoPonto);
                                            break;
                                        case 6:
                                            System.out.println("O destino atual é: " + logNegocio.getClienteCoordF(menuLogin.getPassword()));
                                            System.out.println("Inserir novas coordenadas: ");
                                            Ponto outroPonto;
                                            do {
                                                outroPonto=menuLogin.lerCoordenada();
                                            } while(outroPonto==null);
                                            logNegocio.setClientCoordF(menuLogin.getPassword(),outroPonto);
                                            System.out.println("Ok. Destino atual: " + outroPonto);
                                            break;
                                        case 7:
                                            System.out.println("O meu histórico de alugueres é:\n");
                                            System.out.println(logNegocio.getClienteHistorico(menuLogin.getPassword()));
                                            break;
                                        case 8:
                                            System.out.println("A minha posição atual é: \n");
                                            System.out.println(logNegocio.getCliente(this.menuLogin.getPassword()).getPosicaoI());
                                            break;
                                        case 9:
                                            System.out.println("O meu destino atual é: \n");
                                            System.out.println(logNegocio.getCliente(this.menuLogin.getPassword()).getPosicaoF());
                                            break;
                                        case 10:
                                            System.out.println("A minha informação pessoal é:\n");
                                            System.out.println(logNegocio.getCliente(this.menuLogin.getPassword()));
                                            break;
                                    }
                                } while (this.menuCliente.getOp() != 0);
                                System.out.println("Voltando ao menu de login...");
                                break;
                            case 2:
                                do {
                                    this.menuProprietario.executa();
                                    switch (menuProprietario.getOp()) {
                                        case 1:
                                            String matr1;
                                            try {
                                                List<Veiculo> frotaFill = logNegocio.getVeiculosFill(this.menuLogin.getPassword());
                                                System.out.println(frotaFill);
                                                System.out.println("Encher o depósito de: ");
                                            }
                                            catch (PrintError e) {
                                                System.out.println(e.getMessage());
                                                break;
                                            }
                                            do {
                                                System.out.print("Matrícula: ");
                                                matr1 = menuLogin.leString();
                                            }while(matr1==null);

                                            try {
                                                Veiculo v = logNegocio.getCarro(matr1).clone();
                                                v.fillDeposito();
                                                logNegocio.updateCarro(v);
                                                System.out.println("Ok. Depósito do carro está agora cheio.");
                                            }
                                            catch(PrintError e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Alterar o preço/km de: ");
                                            String matr2;
                                            double novopkm;
                                            List<Veiculo> frotaPreco;
                                            try {
                                                frotaPreco = logNegocio.getVeiculos(this.menuLogin.getPassword());
                                                System.out.println(frotaPreco);
                                                System.out.println("Alterar o preço de:");
                                            }
                                            catch (PrintError e) {
                                                System.out.println(e.getMessage());
                                                break;
                                            }
                                            do {
                                                System.out.print("Matrícula: ");
                                                matr2 = menuLogin.leString();
                                            }while(matr2==null);

                                            try {
                                                Veiculo vxx = rentID(frotaPreco,matr2);
                                                do{
                                                    System.out.print("Novo preço/km: ");
                                                    novopkm = menuLogin.lerDouble();
                                                }while(novopkm==-1);
                                                vxx.setPriceKm(novopkm);
                                                logNegocio.updateCarro(vxx);
                                                System.out.println("Ok. Alterado o novo preco/km.");
                                            }
                                            catch (PrintError e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 3:
                                            int leitInt,ratecl;
                                            String yesNo;
                                            System.out.println("Esta é a lista de Alugueres: ");
                                            System.out.println(this.logNegocio.getProp(this.menuLogin.getPassword()).getQueue());
                                            if(this.logNegocio.getProp(this.menuLogin.getPassword()).getQueue().isEmpty()){
                                                System.out.println("Não há alugueres para aceitar/recusar.");
                                                break;
                                            }
                                            do{
                                                System.out.print("Inserir ID do aluguer: ");
                                                leitInt = this.menuLogin.leInt();
                                            }while(leitInt==-1);
                                            try {
                                                this.logNegocio.isInQueue(this.menuLogin.getPassword(),leitInt);
                                                do{
                                                    System.out.print("Aceitar? [y/n]: ");
                                                    yesNo = this.menuLogin.leYesNo();
                                                }while(yesNo==null);
                                                Aluguer auxAluguer = logNegocio.getProp(this.menuLogin.getPassword()).getFromQueue(leitInt);
                                                boolean decided = this.logNegocio.decideAluguer(this.menuLogin.getPassword(),leitInt,yesNo);
                                                if(decided) {
                                                    do{
                                                        System.out.print("Classificação do Cliente (0-100): ");
                                                        ratecl=this.menuLogin.leInt();
                                                    }while(ratecl==-1);
                                                    this.logNegocio.addClassificacao(auxAluguer.getClienteID(),ratecl,"Cliente");
                                                }
                                            }
                                            catch(PrintError e) {
                                                System.out.println(e.getMessage());
                                                break;
                                            }
                                            break;
                                        case 4:
                                            System.out.println("Inserir o tipo de Veículo: ");
                                            String tipoVeic, matricVeic,marcaVeic;
                                            Ponto posicaoCreate;
                                            double velM,pKm,cKm,depMax;
                                            do {
                                                System.out.print("Tipo: ");
                                                tipoVeic = this.menuLogin.lerTipo();
                                            }while(tipoVeic==null);
                                            do{
                                                posicaoCreate=this.menuLogin.lerCoordenada();
                                            }while(posicaoCreate==null);
                                            do {
                                                System.out.print("Matricula: ");
                                                matricVeic=this.menuLogin.leMatricula();
                                            }while(matricVeic==null);

                                            try {
                                                Veiculo auxVei = this.logNegocio.getCarro(matricVeic);
                                                System.out.println("Já existe um veículo com esta matrícula.");
                                                break;
                                            }
                                            catch(PrintError e) {
                                                do {
                                                    System.out.print("Marca: ");
                                                    marcaVeic = this.menuLogin.leMarca();
                                                }while(marcaVeic==null);
                                                do{
                                                    System.out.print("Velocidade média: ");
                                                    velM = menuLogin.lerDouble();
                                                }while(velM==-1);
                                                do{
                                                    System.out.print("Preço/km: ");
                                                    pKm = menuLogin.lerDouble();
                                                }while(pKm==-1);
                                                do{
                                                    System.out.print("Consumo/km: ");
                                                    cKm = menuLogin.lerDouble();
                                                }while(cKm==-1);
                                                do{
                                                    System.out.print("Autonomia: ");
                                                    depMax = menuLogin.lerDouble();
                                                }while(depMax==-1);
                                                this.logNegocio.createVeiculo(this.menuLogin.getPassword(),tipoVeic,posicaoCreate,matricVeic,marcaVeic,velM,pKm,cKm,depMax);
                                                System.out.println("Ok. Veículo criado.");
                                            }
                                            break;
                                        case 5:
                                            String toremove;
                                            System.out.println("A minha frota atual de veículos é: \n");
                                            List<Veiculo> frotaRemove;
                                            try{
                                                frotaRemove = logNegocio.getVeiculos(this.menuLogin.getPassword());
                                                System.out.println(frotaRemove);
                                            }
                                            catch(PrintError e) {
                                                System.out.println(e.getMessage());
                                                break;
                                            }

                                            System.out.println("O veículo para remover é: \n");

                                            do {
                                                System.out.print("Matrícula: ");
                                                toremove = menuLogin.leString();
                                            }while(toremove==null);

                                            try {
                                                this.logNegocio.updateFrota(this.menuLogin.getPassword(),frotaRemove,toremove);
                                                System.out.println("Ok. Removido da minha frota.");
                                            }
                                            catch(PrintError e) {
                                                System.out.println(e.getMessage());
                                                break;
                                            }
                                            break;
                                        case 6:
                                            System.out.println("Esta é a minha lista de alugueres aceites: ");
                                            System.out.println(this.logNegocio.getProp(this.menuLogin.getPassword()).getHistorico());
                                            break;
                                        case 7:
                                            System.out.println("A minha frota atual de veículos é: \n");
                                            try{
                                                List<Veiculo> frotaAtual = logNegocio.getVeiculos(this.menuLogin.getPassword());
                                                System.out.println(frotaAtual);
                                            }
                                            catch(PrintError e) {
                                                System.out.println(e.getMessage());
                                            }
                                            break;
                                        case 8:
                                            System.out.println("A minha informação pessoal é:\n");
                                            System.out.println(logNegocio.getProp(this.menuLogin.getPassword()));
                                            break;
                                    }
                                } while (this.menuProprietario.getOp() != 0);
                                System.out.println("Voltando ao menu de login...");
                                break;
                        }
                    } while(this.menuLogin.getOp()!=0);
                    System.out.println("Voltando ao menu de sign-in/sign-up...");
                    break;
                case 2:
                    //sign-up
                    break;
            }
        }while(this.menuSign.getOp()!=0);
        System.out.println("Saindo do programa...");
    }
/*
    private Veiculo rentClosest(Cliente client, Map<String,Veiculo> cars) {
        double dist, mindist = Double.MAX_VALUE, distFinal;
        Ponto posI = client.getPosicaoI();
        Ponto posF = client.getPosicaoF();
        distFinal = posF.distancia(posI);
        Ponto poscar;
        Veiculo aux = null;

        for(Veiculo x : cars.values()) {
            poscar = x.getPosicao();
            dist = poscar.distancia(posI);
            if(dist < mindist && x.hasAutonomia(distFinal)) { //tem de ter autonomia
                mindist = dist;
                if(x instanceof Eletrico) {
                    aux = new Eletrico(x);
                }
                else if (x instanceof Hibrido) {
                    aux = new Hibrido(x);
                }
                else {
                    aux = new Gasolina(x);
                }
            }
        }
        if(aux!=null){
            aux.updateAutonomia(distFinal);
            aux.setPosicao(posF);
        }
        return aux;
    }

    private Veiculo rentClosest(Cliente client, Map<String,Veiculo> cars, String tipo) {
        double dist, mindist = Double.MAX_VALUE;
        Ponto posI = client.getPosicaoI();
        Ponto posF = client.getPosicaoF();
        double distFinal = posI.distancia(posF);
        Ponto poscar;
        Veiculo aux = null;

        for(Veiculo x : cars.values()) {
            poscar = x.getPosicao();
            dist = poscar.distancia(posI);
            if(dist < mindist && x.getTipo().equals(tipo) && x.hasAutonomia(distFinal)) { //tem de ter autonomia
                mindist = dist;
                if(x instanceof Eletrico) {
                    aux = new Eletrico(x);
                }
                else if (x instanceof Hibrido) {
                    aux = new Hibrido(x);
                }
                else {
                    aux = new Gasolina(x);
                }
            }
        }
        if(aux!=null) {
            aux.updateAutonomia(distFinal);
            aux.setPosicao(posF);
        }
        return aux;
    }

    private Veiculo rentCheapest(Cliente client, Map<String,Veiculo> cars) {
        double mincusto = Double.MAX_VALUE;
        Veiculo v = null;
        Ponto posF = client.getPosicaoF();
        Ponto posC;
        double pkm, custo,distancia;
        for(Veiculo x: cars.values()) {
            posC = x.getPosicao();
            distancia = posF.distancia(posC);
            pkm = x.getPriceKm();
            custo = pkm*distancia;
            if(custo < mincusto) {
                mincusto = custo;
                if(x instanceof Eletrico) {
                    v = new Eletrico(x);
                }
                else if (x instanceof Hibrido) {
                    v = new Hibrido(x);
                }
                else {
                    v = new Gasolina(x);
                }
            }
        }
        return v;
    }

    //este e para a distancia no maximo de raio X
    private Veiculo rentCheapest(Cliente client, Map<String,Veiculo> cars, double raio) {
        double mincusto = Double.MAX_VALUE;
        Veiculo v = null;
        Ponto posI = client.getPosicaoI();
        Ponto posF = client.getPosicaoF();
        Ponto posC;
        double pkm, custo;
        double distancia;
        for(Veiculo x: cars.values()) {
            posC = x.getPosicao();
            distancia = posC.distancia(posF);
            pkm = x.getPriceKm();
            custo = pkm*distancia;
            if(custo < mincusto && posI.distancia(posC)<=raio) {
                mincusto = custo;
                if(x instanceof Eletrico) {
                    v = new Eletrico(x);
                }
                else if (x instanceof Hibrido) {
                    v = new Hibrido(x);
                }
                else {
                    v = new Gasolina(x);
                }
            }
        }
        return v;
    }

    private Veiculo rentAutonomy(Cliente client, Map<String,Veiculo> cars, double autonomy) throws PrintError {
        Veiculo v = null;
        double autonomia;
        for(Veiculo x : cars.values()) {
            autonomia = x.getDepositoAtual();
            if(autonomia==autonomy) {
                if(x instanceof Eletrico) {
                    v = new Eletrico(x);
                    break;
                }
                else if (x instanceof Hibrido) {
                    v = new Hibrido(x);
                    break;
                }
                else {
                    v = new Gasolina(x);
                    break;
                }
            }
        }
        if(v==null) {
            throw new PrintError("Não existe carro que satisfaça as condições");
        }
        return v;
    }
*/
    private Veiculo rentID(List<Veiculo> cars, String ID) throws PrintError {
            for(Veiculo ret: cars) {
                if(ret.getID().equals(ID)) {
                    return ret.clone();
                }
            }
            throw new PrintError("Não é possivel obter o carro pedido.");
    }

    private List<Veiculo> availableCars(Cliente client, Map<String,Veiculo> cars, double d) throws PrintError{
        Ponto p = client.getPosicaoI();
        Ponto pf;
        double dist;
        List<Veiculo> ret = new ArrayList<>();
        for(Veiculo v: cars.values()) {
            pf = v.getPosicao();
            dist = pf.distancia(p);
            if(dist<=d) {
                ret.add(v.clone());
            }
        }
       if(ret.isEmpty()) {
           throw new PrintError("Não é possível encontrar um carro no raio desejado.");
       }
       else {
           return ret;
       }
    }

    private void alterarPKM(Map<String,Veiculo> cars, String ID, double pkm) {
        Veiculo x = cars.get(ID).clone();
        x.setPriceKm(pkm);
        cars.put(ID,x);
    }

    private void fillDeposit(Map<String,Veiculo> cars, String ID) {
        Veiculo x = cars.get(ID).clone();
        x.setDepositoAtual(x.getDepositoMax());
        cars.put(ID,x);
    }

}
