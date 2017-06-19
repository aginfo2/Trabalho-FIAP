package br.com.fiap.banco.excecao;

public class ValorEmprestimoExcedidoExcecao extends Exception {
	
	private static final long serialVersionUID = -3674565706679030569L;

	public ValorEmprestimoExcedidoExcecao() {
		super("O valor de empr�stimo solicitado excede o limite m�ximo permitido");
	}

}
