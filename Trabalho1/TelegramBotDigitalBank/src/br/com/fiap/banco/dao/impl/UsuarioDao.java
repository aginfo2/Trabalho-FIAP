package br.com.fiap.banco.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.fiap.banco.entidades.Usuario;
import br.com.fiap.generico.dao.DaoGenerico;

/**
 * Classe respons�vel por tratar o DAO do Usuario
 *
 */
public class UsuarioDao extends DaoGenerico<Usuario> {

	public UsuarioDao() {
		super(Usuario.class);
	}

	/**
	 * Cria um novo usu�rio
	 * 
	 * @param usuario Objeto Usuario que ser� persistido em BD
	 */
	public void criarUsuario(Usuario usuario) {
		super.adicionar(usuario);
	}

	/**
	 * Altera os dados do usu�rio
	 * 
	 * @param usuario Objeto Usuario que ser� alterado em BD
	 */
	public void alterarUsuario(Usuario usuario) {
		super.atualizar(usuario);
	}

	/**
	 * Busca o usu�rio pelo CPF
	 * 
	 * @param cpf CPF do usu�rio
	 * 
	 * @return Objeto Usuario
	 */
	public Usuario buscarUsuario(String cpf) {
		TypedQuery<Usuario> query = super.em.createQuery("select u from Usuario u where cpf = '" + cpf + "'", Usuario.class);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
