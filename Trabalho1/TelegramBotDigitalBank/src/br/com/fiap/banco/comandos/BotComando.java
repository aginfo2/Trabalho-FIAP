package br.com.fiap.banco.comandos;

import java.util.List;

import br.com.fiap.banco.dados.EmprestimoDetalhe;
import br.com.fiap.banco.dados.TransacaoDetalhe;
import br.com.fiap.banco.entidades.Transacao;
import br.com.fiap.banco.entidades.Usuario;
import br.com.fiap.banco.excecao.ContaInexistenteExcecao;
import br.com.fiap.banco.excecao.EmprestimoAbertoExcecao;
import br.com.fiap.banco.excecao.PrazoEmprestimoExcedidoExcecao;
import br.com.fiap.banco.excecao.SaldoInsuficienteExcecao;
import br.com.fiap.banco.excecao.UsuarioDuplicadoExcecao;
import br.com.fiap.banco.excecao.ValorEmprestimoExcedidoExcecao;

/**
 * Classe respons�vel por organizar todos os comandos que podem ser utilizados pelo BOT
 *
 */
public class BotComando {
	
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
		(new ContaComando()).criarConta(idTelegram, nome, sobrenome, telefone, cpf, email);
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
	public synchronized void modificarConta(long idTelegram, String cpf, String email) throws ContaInexistenteExcecao {
		(new ContaComando()).alterarConta(idTelegram, cpf, email);
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
	 * @throws UsuarioDuplicadoExcecao Se o usu�rio j� existir no BD
	 */
	public synchronized void incluirDependente(long idTelegram, String nome, String sobrenome, String telefone, String cpf, String email) throws ContaInexistenteExcecao, UsuarioDuplicadoExcecao {
		(new ContaComando()).incluirDependente(idTelegram, nome, sobrenome, telefone, cpf, email);
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
	public synchronized List<Usuario> listarUsuariosEDependentes(long idTelegram) throws ContaInexistenteExcecao {
		return (new ContaComando()).listarUsuarios(idTelegram);
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
		(new ContaComando()).realizarDeposito(idTelegram, valor);
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
		(new ContaComando()).realizarSaque(idTelegram, valor);
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
		return (new ContaComando()).verificarSaldo(idTelegram);
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
	public synchronized List<Transacao> verificarExtrato(long idTelegram) throws SaldoInsuficienteExcecao, ContaInexistenteExcecao {
		return (new ContaComando()).verificacaoExtrato(idTelegram);
	}
	
	/**
	 * Verifica e informa o valor m�ximo que o usu�rio pode solicitar de empr�stimo
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return Valor m�ximo que o usu�rio pode solicitar de empr�stimo
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized double verificarValorMaximoEmprestimo(long idTelegram) throws ContaInexistenteExcecao {
		return (new EmprestimoComando()).verificarValorMaximoEmprestimo(idTelegram);
	}
	
	/**
	 * Solicita um empr�stimo ao banco que ser� pago mensalmente pelo usu�rio
	 * 
	 * @param idTelegram Id do Telegram
	 * @param valor Valor que est� sendo solicitado de empr�stimo
	 * @param prazo Prazo para pagamento do empr�stimo
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 * @throws ValorEmprestimoExcedidoExcecao Se exceder o valor m�ximo permitido para empr�stimo
	 * @throws PrazoEmprestimoExcedidoExcecao Se ultrapassar o prazo m�ximo permitido para pagamento do empr�stimo
	 * @throws SaldoInsuficienteExcecao Se n�o houver saldo suficiente para concluir a opera��o
	 * @throws EmprestimoAbertoExcecao Se j� houver algum empr�stimo em aberto nessa conta
	 */
	public synchronized void solicitarEmprestimo(long idTelegram, double valor, int prazo) throws ContaInexistenteExcecao, ValorEmprestimoExcedidoExcecao, PrazoEmprestimoExcedidoExcecao, SaldoInsuficienteExcecao, EmprestimoAbertoExcecao {
		(new EmprestimoComando()).solicitarEmprestimo(idTelegram, valor, prazo);
	}
	
	/**
	 * Verifica e informa o saldo devedor e o prazo restante para o pagamento de um empr�stimo
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return O saldo devedor e o prazo restante para o pagamento de um empr�stimo
	 */
	public synchronized EmprestimoDetalhe verificarSaldoDevedorPrazoEmprestimo(long idTelegram) {
		return (new EmprestimoComando()).buscarSaldoDevedorPrazoEmprestimo(idTelegram);
	}
	
	/**
	 * Lista todos os lan�amentos que foram realizadas na conta do usu�rio
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return Os lan�amentos realizados e a somat�ria dos mesmos
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized TransacaoDetalhe listarLancamentos(long idTelegram) throws ContaInexistenteExcecao {
		return (new TransacaoComando()).listarLancamentos(idTelegram);
	}
	
	/**
	 * Lista todas as retiradas que foram realizadas na conta do usu�rio
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return As retiradas realizadas e a somat�ria das mesmas
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized TransacaoDetalhe listarRetiradas(long idTelegram) throws ContaInexistenteExcecao {
		return (new TransacaoComando()).listarRetiradas(idTelegram);
	}
	
	/**
	 * Lista todas as tarifas j� pagas pelo usu�rio
	 * 
	 * @param idTelegram Id do Telegram
	 * 
	 * @return As tarifas pagas e a somat�ria das mesmas
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	public synchronized TransacaoDetalhe listarTarifas(long idTelegram) throws ContaInexistenteExcecao {
		return (new TransacaoComando()).listarTarifas(idTelegram);
	}

}
