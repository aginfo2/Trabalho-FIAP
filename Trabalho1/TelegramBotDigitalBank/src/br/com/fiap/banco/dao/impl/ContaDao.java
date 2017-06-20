package br.com.fiap.banco.dao.impl;

import br.com.fiap.banco.entidades.Conta;
import br.com.fiap.generico.dao.DaoGenerico;

/**
 * Classe respons�vel por tratar o DAO da Conta
 *
 */
public class ContaDao extends DaoGenerico<Conta> {

	public ContaDao() {
		super(Conta.class);
	}

	/**
	 * Cria uma nova conta para o usu�rio
	 * 
	 * @param conta Objeto Conta que ser� persistido em BD
	 * 
	 * @return <code>true</code> se a conta n�o existir e conseguir criar a nova conta, se n�o <code>false</code>
	 */
	public boolean criarConta(Conta conta) {
		if (conta.getNumero() == null || !temConta(conta.getNumero())) {
			super.adicionar(conta);
			return true;
		}
		return false;
	}

	/**
	 * Altera os dados da conta
	 * 
	 * @param conta Objeto Conta que ser� alterado em BD
	 */
	public void alterarConta(Conta conta) {
		super.atualizar(conta);
	}

	/**
	 * Verifica se a conta existe no BD
	 * 
	 * @param id Id do Telegram
	 * 
	 * @return <code>true</code> se a conta existir, se n�o <code>false</code>
	 */
	public boolean temConta(long id) {
		return (super.buscar(id) != null);
	}

	/**
	 * Busca a conta informada no BD
	 * 
	 * @param id Id do Telegram
	 * 
	 * @return Objeto Conta
	 */
	public Conta buscarConta(long id) {
		return super.buscar(id);
	}

}
