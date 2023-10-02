package banco;


public class ContaCorrente extends Conta {
    
    private double limite;

    public ContaCorrente(String titular, double limite) {
        super(titular);
        this.limite = limite;
    }

    @Override
    public void sacar(double valor) {
        if (saldo + limite >= valor) {
            saldo -= valor;
        } else {
            System.out.println("Saldo insuficiente.");
        }
    }
}

