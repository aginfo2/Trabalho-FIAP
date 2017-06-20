package br.com.fiap.banco.constantes;

/**
 * Enum respons�vel pelos custos de servi�o e juros
 *
 */
public enum Tarifas {
	
	/**
	 * Constante referente ao custo de servi�o do saque
	 */
	SAQUE		(2.5,	0),
	/**
	 * Constante referente ao custo de servi�o do extrato
	 */
	EXTRATO		(1.0,	0),
	/**
	 * Constante referente ao custo de servi�o e juros mensais do empr�stimo
	 */
	EMPRESTIMO	(15.0,	5);
	
	private double custoServico;
	private int jurosMensais;

	private Tarifas(double custoServico, int jurosMensais) {
		this.custoServico = custoServico;
		this.jurosMensais = jurosMensais;
	}

	public double getCustoServico() {
		return custoServico;
	}

	public int getJurosMensais() {
		return jurosMensais;
	}
}
