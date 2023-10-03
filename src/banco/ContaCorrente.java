package banco;

import main.Main;

public class ContaCorrente extends Conta {
    
    private double limite;

    public ContaCorrente(String titular, double limite) {
        super(titular);
        this.limite = limite;
    }

    @Override
    public void sacar(double valor) {

        Main main = new Main();

        if (saldo + limite >= valor) {
            saldo -= valor;
        } else {
            main.exibirAlerta("Saldo insuficiente. Seu limite foi ultrapassado!");
        }
    }

   
}

