package banco;


public abstract class Conta{
    
    protected String titular;
    protected double saldo;

    public Conta(String titular) {
        this.titular = titular;
        this.saldo = 0.0;
    }

    public void depositar(double valor) {
        saldo += valor;
    }

    public abstract void sacar(double valor);

    public double getSaldo() {
        return saldo;
    }

    public String getTitular() {
        return titular;
    }

}