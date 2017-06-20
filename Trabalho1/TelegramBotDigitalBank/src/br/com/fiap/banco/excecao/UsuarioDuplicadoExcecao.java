package br.com.fiap.banco.excecao;

/**
 * Exce��o para tratamento quando solicitar a inclus�o de um usu�rio que j� est� cadastrado
 *
 */
public class UsuarioDuplicadoExcecao extends Exception {
	
	private static final long serialVersionUID = -9196841502368177309L;

	public UsuarioDuplicadoExcecao() {
		super("J� existe um usu�rio com esse CPF cadastrado");
	}

}
