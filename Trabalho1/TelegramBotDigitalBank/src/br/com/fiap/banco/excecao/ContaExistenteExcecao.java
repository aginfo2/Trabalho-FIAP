package br.com.fiap.banco.excecao;

/**
 * Exce��o para evitar que sejam criadas v�rias contas para um mesmo usu�rio
 *
 */
public class ContaExistenteExcecao extends Exception {
	
	private static final long serialVersionUID = 4282575057920376690L;

	public ContaExistenteExcecao() {
		super("J� existe uma conta para esse usu�rio. N�o � poss�vel criar uma nova conta!");
	}

}
