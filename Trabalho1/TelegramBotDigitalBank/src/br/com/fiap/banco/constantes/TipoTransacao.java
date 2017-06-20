package br.com.fiap.banco.constantes;

/**
 * Enum respons�vel por mapear os tipos de transa��es existentes
 *
 */
public enum TipoTransacao {

	/**
	 * Constante que define a transa��o de saque
	 */
	SAQUE(1),
	/**
	 * Constante que define a transa��o de dep�sito
	 */
	DEPOSITO(2),
	/**
	 * Constante que define a transa��o de tarifas pagas nas opera��es
	 */
	TARIFA(3),
	/**
	 * Constante que define a transa��o de solicita��o de empr�stimo
	 */
	EMPRESTIMO(4),
	/**
	 * Constante que define a transa��o de juros pagos nas parcelas do empr�stimo
	 */
	JUROS(5),
	/**
	 * Constante que define a transa��o de parcelas pagas do empr�stimo
	 */
	PAGAMENTO_EMPRESTIMO(6);

	private int codigo;

	private TipoTransacao(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo() {
		return codigo;
	}

	public static TipoTransacao getTipoTransacao(int codigo) {
		if (codigo > 0) {
			for (TipoTransacao tipoTransacao : values()) {
				if (tipoTransacao.codigo == codigo) {
					return tipoTransacao;
				}
			}
		}
		return null;
	}

}
