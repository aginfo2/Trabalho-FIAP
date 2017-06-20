package br.com.fiap.banco.excecao;

/**
 * Exce��o para tratamento quando tentar realizar uma opera��o em uma conta n�o existente
 *
 */
public class ContaInexistenteExcecao extends Exception {

	private static final long serialVersionUID = 8985501046491187447L;

	public ContaInexistenteExcecao() {
		super("A conta informada n�o existe!");
	}

}
