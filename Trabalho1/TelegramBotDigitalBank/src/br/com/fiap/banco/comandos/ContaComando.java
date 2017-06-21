package br.com.fiap.banco.comandos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.fiap.banco.constantes.Tarifas;
import br.com.fiap.banco.constantes.TipoTransacao;
import br.com.fiap.banco.constantes.TipoUsuario;
import br.com.fiap.banco.dao.impl.ContaDao;
import br.com.fiap.banco.dao.impl.TransacaoDao;
import br.com.fiap.banco.dao.impl.UsuarioDao;
import br.com.fiap.banco.entidades.Conta;
import br.com.fiap.banco.entidades.Transacao;
import br.com.fiap.banco.entidades.Usuario;
import br.com.fiap.banco.excecao.ContaInexistenteExcecao;
import br.com.fiap.banco.excecao.SaldoInsuficienteExcecao;
import br.com.fiap.banco.excecao.UsuarioDuplicadoExcecao;

/**
 * Classe respons�vel por organizar todos os comando que s�o utilizados na Conta
 *
 */
class ContaComando {

	/**
	 * Retorna a conta a partir do Id do Telegram
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return O Objeto Conta referente ao id informado
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized Conta buscarConta(long idTelegram) throws ContaInexistenteExcecao {
		Conta conta = null;
		
		try (ContaDao contaDao = new ContaDao();) {
			conta = contaDao.buscarConta(idTelegram);
			
			if(conta == null) {
				throw new ContaInexistenteExcecao();
			}
		}
		
		return conta;
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
		Conta conta = new Conta();
		conta.setNumero(idTelegram);
		conta.setDataAbertura(LocalDate.now());
		conta.setSaldo(0.0d);

		try (ContaDao contaDao = new ContaDao();) {
			if (contaDao.criarConta(conta)) {
				try (UsuarioDao usuarioDao = new UsuarioDao();) {
					Usuario usuario = this.criarUsuario(nome, sobrenome, telefone, cpf, email, TipoUsuario.PRINCIPAL, conta);
					
					usuarioDao.criarUsuario(usuario);
				}
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
		try (UsuarioDao usuarioDao = new UsuarioDao()) {
			Conta conta = this.buscarConta(idTelegram);
			
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
		try (UsuarioDao usuarioDao = new UsuarioDao();) {
			Conta conta = this.buscarConta(idTelegram);
			
			Usuario usuario = this.criarUsuario(nome, sobrenome, telefone, cpf, email, TipoUsuario.DEPENDENTE, conta);
			
			if(!usuarioDao.criarUsuario(usuario)) {
				throw new UsuarioDuplicadoExcecao();
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
		Conta conta = this.buscarConta(idTelegram);
		
		return conta.getUsuarios();
	}
	
	/**
	 * Atualiza o saldo de acordo com o tipo de transa��o
	 * 
	 * @param idTelegram ID do Telegram
	 * @param valor Valor que ser� utilizado para somar ou subtrair do saldo
	 * @param tipoTransacao Tipo de transa��o que est� sendo realizada
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized void atualizarSaldo(long idTelegram, double valor, TipoTransacao tipoTransacao) throws ContaInexistenteExcecao {
		Conta conta = this.buscarConta(idTelegram);
		
		double saldo = conta.getSaldo();
		
		switch (tipoTransacao) {
			case DEPOSITO:
			case EMPRESTIMO:
				saldo += valor;
				break;
			case SAQUE:
			case TARIFA:
			case JUROS:
			case PAGAMENTO_EMPRESTIMO:
				saldo -= valor;
				break;
		}
		
		conta.setSaldo(saldo);
		
		try (ContaDao contaDao = new ContaDao();) {
			contaDao.alterarConta(conta);
		}

		Transacao transacao = new Transacao();
		
		transacao.setConta(conta);
		transacao.setDataHora(LocalDateTime.now());
		transacao.setTipoTransacao(tipoTransacao.getCodigo());
		transacao.setValor(valor);
		
		try (TransacaoDao transacaoDao = new TransacaoDao();) {
			transacaoDao.adicionarTransacao(transacao);
		}
	}
	
	/**
	 * Busca e devolve o saldo dispon�vel na conta do usu�rio
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return O saldo dispon�vel
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized double verificarSaldo(long idTelegram) throws ContaInexistenteExcecao {
		Conta conta = this.buscarConta(idTelegram);
		
		return conta.getSaldo();
	}

	/**
	 * Busca e devolve todas as transa��es realizadas na conta do usu�rio
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return A lista com todas as transa��es
	 * 
	 * @throws SaldoInsuficienteExcecao Se n�o houver saldo suficiente para concluir a opera��o
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized List<Transacao> verificacaoExtrato(long idTelegram) throws SaldoInsuficienteExcecao, ContaInexistenteExcecao {
		if (Tarifas.EXTRATO.getCustoServico() <= this.verificarSaldo(idTelegram)) {
			this.atualizarSaldo(idTelegram, Tarifas.EXTRATO.getCustoServico(), TipoTransacao.TARIFA);

			Conta conta = this.buscarConta(idTelegram);

			return conta.getTransacoes();
		} else {
			throw new SaldoInsuficienteExcecao();
		}
	}
	
	/**
	 * Realiza um dep�sito na conta do usu�rio
	 * 
	 * @param idTelegram ID do Telegram
	 * @param valor Valor � ser depositado
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized void realizarDeposito(long idTelegram, double valor) throws ContaInexistenteExcecao {
		this.atualizarSaldo(idTelegram, valor, TipoTransacao.DEPOSITO);
	}

	/**
	 * Realiza um saque na conta do usu�rio
	 * 
	 * @param idTelegram ID do Telegram
	 * @param valor Valor � ser sacado
	 * 
	 * @throws SaldoInsuficienteExcecao Se n�o houver saldo suficiente para concluir a opera��o
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized void realizarSaque(long idTelegram, double valor) throws SaldoInsuficienteExcecao, ContaInexistenteExcecao {
		if (this.verificarSaldo(idTelegram) > (valor + Tarifas.SAQUE.getCustoServico())) {
			this.atualizarSaldo(idTelegram, valor, TipoTransacao.SAQUE);
			this.atualizarSaldo(idTelegram, Tarifas.SAQUE.getCustoServico(), TipoTransacao.TARIFA);
		} else {
			throw new SaldoInsuficienteExcecao();
		}
	}
	
	/**
	 * Cria um novo usu�rio
	 * 
	 * @param nome Nome do usu�rio
	 * @param sobrenome Sobrenome do usu�rio
	 * @param telefone N�mero de telefone do usu�rio
	 * @param cpf CPF do usu�rio
	 * @param email Email do usu�rio
	 * @param tipoUsuario Tipo de usu�rio da conta (principal ou dependente)
	 * @param conta A conta � qual esse usu�rio pertence
	 * 
	 * @return O usu�rio que foi criado
	 */
	private synchronized Usuario criarUsuario(String nome, String sobrenome, String telefone, String cpf, String email, TipoUsuario tipoUsuario, Conta conta) {
		Usuario usuario = new Usuario();
		
		usuario.setNome(nome + " " + sobrenome);
		usuario.setTelefone(telefone);
		usuario.setCpf(cpf);
		usuario.setEmail(email);
		usuario.setTipoUsuario(tipoUsuario.getCodigo());
		usuario.setConta(conta);
		
		return usuario;
	}

}
