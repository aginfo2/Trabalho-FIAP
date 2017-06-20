package br.com.fiap.banco.excecao;

/**
 * Exce��o para tratamento quando solicitar um empr�stimo com o prazo maior que o permitido
 *
 */
public class PrazoEmprestimoExcedidoExcecao extends Exception {
	
	private static final long serialVersionUID = -6576489931121833674L;

	public PrazoEmprestimoExcedidoExcecao() {
		super("O prazo m�ximo para pagamento do empr�stimo n�o pode exceder 36 meses (3 anos)");
	}

}
