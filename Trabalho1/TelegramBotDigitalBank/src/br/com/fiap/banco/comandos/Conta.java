package br.com.fiap.banco.comandos;

import java.util.List;

import br.com.fiap.banco.entidades.Usuario;

public class Conta {
	
	public boolean temConta(long id) {
		//TODO Verificar no BD se o ID existe
		return true;
	}
	
	public void criarConta(long id, String nome, String sobrenome, String telefone, String cpf, String email) {
		//TODO Persistir as informa��es do cliente no BD
	}
	
	public void alterarConta(long id, String cpf, String email) {
		//TODO Atualizar o BD com as novas informa��es
	}
	
	public void incluirDependente(long id, String nome, String sobrenome, String telefone, String cpf, String email) {
		//XXX Definir como vai ter todas as informa��es do dependente como o ID, por exemplo!!!
	}
	
	public List<Usuario> listarUsuarios(long id) {
		//TODO Listar todos os usu�rios e dependentes com base na conta informada
		return null;
	}

}
