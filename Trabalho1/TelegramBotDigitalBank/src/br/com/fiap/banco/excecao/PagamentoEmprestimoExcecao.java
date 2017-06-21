package br.com.fiap.banco.excecao;

/**
 * Exce��o para tratamento quando tentar pagar uma parcela e n�o existir ou j� estiver paga
 *
 */
public class PagamentoEmprestimoExcecao extends Exception {

	private static final long serialVersionUID = 4497697016628439950L;

	public PagamentoEmprestimoExcecao() {
		super("N�o � poss�vel realizar o pagamento da parcela selecionada. A parcela n�o existe ou j� est� paga.");
	}
}
