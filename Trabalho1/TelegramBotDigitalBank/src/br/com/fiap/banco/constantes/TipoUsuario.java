package br.com.fiap.banco.constantes;

/**
 * Enum respons�vel por mapear os tipos de usu�rios existentes
 *
 */
public enum TipoUsuario {

	/**
	 * Constante que define o usu�rio principal da conta
	 */
	PRINCIPAL(1),
	/**
	 * Constante que define um usu�rio dependente da conta
	 */
	DEPENDENTE(2);

	private int codigo;

	private TipoUsuario(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo() {
		return codigo;
	}

	public static TipoUsuario getTipoUsuario(int codigo) {
		if (codigo > 0) {
			for (TipoUsuario tipoUsuario : values()) {
				if (tipoUsuario.codigo == codigo) {
					return tipoUsuario;
				}
			}
		}
		return null;
	}

}
