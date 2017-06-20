package br.com.fiap.banco.comandos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.banco.constantes.TipoUsuario;
import br.com.fiap.banco.dao.impl.ContaDao;
import br.com.fiap.banco.dao.impl.UsuarioDao;
import br.com.fiap.banco.entidades.Conta;
import br.com.fiap.banco.entidades.Usuario;
import br.com.fiap.banco.excecao.ContaInexistenteExcecao;
import br.com.fiap.banco.excecao.UsuarioDuplicadoExcecao;

/**
 * Classe respons�vel por organizar todos os comando que s�o utilizados na Conta
 *
 */
class ContaComando {

	/**
	 * Verifica se um usu�rio j� tem uma conta no banco
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return <code>true</code> se a conta existir
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized boolean temConta(long idTelegram) throws ContaInexistenteExcecao {
		EmprestimoComando emprestimoComando = new EmprestimoComando();
		
		try (ContaDao contaDao = new ContaDao();) {
			if(!contaDao.temConta(idTelegram)) {
				throw new ContaInexistenteExcecao();
			} else {
				if(emprestimoComando.verificarEmprestimoAberto(idTelegram)) {
					emprestimoComando.pagarEmprestimosVencidos(idTelegram);
				}
			}
		}
		return true;
	}

	/**
	 * Cria uma nova conta para o usu�rio
	 * 
	 * @param idTelegram ID do Telegram que ser� usado como n�mero da conta
	 * @param nome Nome do usu�rio
	 * @param sobrenome Sobrenome do usu�rio
	 * @param telefone N�mero de telefone do usu�rio
	 * @param cpf CPF do usu�rio
	 * @param email Email do usu�rio
	 */
	public synchronized void criarConta(long idTelegram, String nome, String sobrenome, String telefone, String cpf, String email) {
		Usuario usuario = this.criarUsuario(nome, sobrenome, telefone, cpf, email);
		usuario.setTipoUsuario(TipoUsuario.PRINCIPAL.getCodigo());

		Conta conta = new Conta();
		conta.setNumero(idTelegram);
		conta.setDataAbertura(LocalDate.now());
		conta.setSaldo(0.0d);

		usuario.setConta(conta);

		try (UsuarioDao usuarioDao = new UsuarioDao(); ContaDao contaDao = new ContaDao();) {
			if (contaDao.criarConta(conta)) {
				usuarioDao.criarUsuario(usuario);
			}
		}
	}

	/**
	 * Modifica o CPF e Email do usu�rio
	 * 
	 * @param idTelegram ID do Telegram
	 * @param cpf CPF do usu�rio
	 * @param email Email do usu�rio
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized void alterarConta(long idTelegram, String cpf, String email) throws ContaInexistenteExcecao {
		if (this.temConta(idTelegram)) {
			try (ContaDao contaDao = new ContaDao(); UsuarioDao usuarioDao = new UsuarioDao()) {
				Conta conta = contaDao.buscarConta(idTelegram);
				
				List<Usuario> usuarios = conta.getUsuarios();
				
				for (Usuario usuario : usuarios) {
					if(usuario.getTipoUsuario() == TipoUsuario.PRINCIPAL.getCodigo()) {
						usuario.setCpf(cpf);
						usuario.setEmail(email);
						
						usuarioDao.alterarUsuario(usuario);
					}
				}
			}
		}
	}

	/**
	 * Inclui um novo dependente atrelado ao ID do Telegram do usu�rio principal
	 * 
	 * @param idTelegram ID do Telegram
	 * @param nome Nome do dependente
	 * @param sobrenome Sobrenome do dependente
	 * @param telefone N�mero de telefone do dependente
	 * @param cpf CPF do dependente
	 * @param email Email do dependente
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 * @throws UsuarioDuplicadoExcecao Se o dependente j� existir no BD
	 */
	public synchronized void incluirDependente(long idTelegram, String nome, String sobrenome, String telefone, String cpf, String email) throws ContaInexistenteExcecao, UsuarioDuplicadoExcecao {
		if (this.temConta(idTelegram)) {
			try (UsuarioDao usuarioDao = new UsuarioDao(); ContaDao contaDao = new ContaDao();) {
				Conta conta = contaDao.buscarConta(idTelegram);
				
				Usuario usuarioBD = usuarioDao.buscarUsuario(cpf);
				
				if(usuarioBD == null) {
					Usuario usuario = this.criarUsuario(nome, sobrenome, telefone, cpf, email);
					usuario.setTipoUsuario(TipoUsuario.DEPENDENTE.getCodigo());
					
					usuario.setConta(conta);
					
					usuarioDao.criarUsuario(usuario);
				} else {
					throw new UsuarioDuplicadoExcecao();
				}
			}
		}

	}

	/**
	 * Busca e devolve todos os usu�rio e dependentes de uma conta
	 * 
	 * @param idTelegram ID do Telegram
	 * 
	 * @return A lista com os usu�rios e dependentes
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized List<Usuario> listarUsuarios(long idTelegram) throws ContaInexistenteExcecao {
		if (this.temConta(idTelegram)) {
			try (ContaDao contaDao = new ContaDao(); UsuarioDao usuarioDao = new UsuarioDao();) {
				Conta conta = contaDao.buscarConta(idTelegram);
				return conta.getUsuarios();
			}
		}
		return new ArrayList<>();
	}
	
	/**
	 * Cria um novo usu�rio
	 * 
	 * @param nome Nome do usu�rio
	 * @param sobrenome Sobrenome do usu�rio
	 * @param telefone N�mero de telefone do usu�rio
	 * @param cpf CPF do usu�rio
	 * @param email Email do usu�rio
	 * 
	 * @return O usu�rio que foi criado
	 */
	private synchronized Usuario criarUsuario(String nome, String sobrenome, String telefone, String cpf, String email) {
		Usuario usuario = new Usuario();
		
		usuario.setNome(nome + " " + sobrenome);
		usuario.setTelefone(telefone);
		usuario.setCpf(cpf);
		usuario.setEmail(email);
		
		return usuario;
	}

}
