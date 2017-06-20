package br.com.fiap.banco.excecao;

/**
 * Exce��o para tratamento quando tentar realizar uma opera��o na qual n�o tenha saldo suficiente
 *
 */
public class SaldoInsuficienteExcecao extends Exception {

	private static final long serialVersionUID = -8600292077740904195L;

	public SaldoInsuficienteExcecao() {
		super("Saldo insuficiente!");
	}
}
