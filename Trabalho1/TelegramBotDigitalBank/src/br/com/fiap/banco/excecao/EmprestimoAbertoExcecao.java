package br.com.fiap.banco.excecao;

public class EmprestimoAbertoExcecao extends Exception {
	
	private static final long serialVersionUID = -8556625379904529166L;

	public EmprestimoAbertoExcecao() {
		super("O usu�rio j� possui um empr�stimo em aberto");
	}

}
