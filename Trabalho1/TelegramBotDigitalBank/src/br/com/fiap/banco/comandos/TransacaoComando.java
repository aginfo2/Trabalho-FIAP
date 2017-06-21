package br.com.fiap.banco.comandos;

import br.com.fiap.banco.constantes.TipoTransacao;
import br.com.fiap.banco.dados.TransacaoDetalhe;
import br.com.fiap.banco.dao.impl.TransacaoDao;
import br.com.fiap.banco.entidades.Conta;
import br.com.fiap.banco.entidades.Transacao;
import br.com.fiap.banco.excecao.ContaInexistenteExcecao;

/**
 * Classe respons�vel por organizar todos os comando que s�o utilizados nas Transa��es
 *
 */
class TransacaoComando {

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
		//TODO Revisar para incluir empr�stimo
		return this.listarTransacao(idTelegram, TipoTransacao.DEPOSITO);
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
		//TODO Revisar para incluir empr�stimo
		return this.listarTransacao(idTelegram, TipoTransacao.SAQUE);
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
		//TODO Revisar para incluir empr�stimo
		return this.listarTransacao(idTelegram, TipoTransacao.TARIFA);
	}
	
	/**
	 * Lista as transa��es de acordo com o tipo informado
	 * 
	 * @param idTelegram Id do Telegram
	 * @param tipoTransacao Tipo de transa��o que ser� listada
	 * 
	 * @return A lista da transa��o solicitada e a somat�ria da mesma
	 * 
	 * @throws ContaInexistenteExcecao Se n�o existir a conta informada
	 */
	private synchronized TransacaoDetalhe listarTransacao(long idTelegram, TipoTransacao tipoTransacao) throws ContaInexistenteExcecao {
		//TODO Revisar para incluir empr�stimo
		ContaComando contaComando = new ContaComando();
		TransacaoDetalhe transacaoDetalhe = new TransacaoDetalhe();
		
		try (TransacaoDao transacaoDao = new TransacaoDao();) {
			Conta conta = contaComando.buscarConta(idTelegram);
			
			transacaoDetalhe.setTransacoes(transacaoDao.buscarTransacoes(conta.getNumero(), tipoTransacao));
			
			double somatorio = 0.0d;
			
			for (Transacao transacao : transacaoDetalhe.getTransacoes()) {
				somatorio += transacao.getValor();
			}
			
			transacaoDetalhe.setSomatorio(somatorio);
		}
		
		return transacaoDetalhe;
	}

}
