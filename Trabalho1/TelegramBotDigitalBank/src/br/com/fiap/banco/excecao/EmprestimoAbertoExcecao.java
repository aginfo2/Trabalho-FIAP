package br.com.fiap.banco.excecao;

/**
 * Exce��o para tratamento quando solicitar um novo empr�stimo sem ter quitado um aberto
 *
 */
public class EmprestimoAbertoExcecao extends Exception {
	
	private static final long serialVersionUID = -8556625379904529166L;

	public EmprestimoAbertoExcecao() {
		super("O usu�rio j� possui um empr�stimo em aberto");
	}

}
