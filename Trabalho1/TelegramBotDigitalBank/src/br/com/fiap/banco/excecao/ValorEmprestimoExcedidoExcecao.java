package br.com.fiap.banco.excecao;

/**
 * Exce��o para tratamento quando solicitar um empr�stimo com um valor maior que o m�ximo permitido para a conta
 *
 */
public class ValorEmprestimoExcedidoExcecao extends Exception {
	
	private static final long serialVersionUID = -3674565706679030569L;

	public ValorEmprestimoExcedidoExcecao() {
		super("O valor de empr�stimo solicitado excede o limite m�ximo permitido");
	}

}
