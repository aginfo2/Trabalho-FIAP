package br.com.fiap.bot.integradores.impl;

import com.pengrad.telegrambot.model.Chat;

import br.com.fiap.banco.comandos.BotComando;
import br.com.fiap.banco.constantes.TipoTransacao;
import br.com.fiap.banco.dados.TransacaoDetalhe;
import br.com.fiap.banco.entidades.Transacao;
import br.com.fiap.banco.excecao.ContaInexistenteExcecao;
import br.com.fiap.bot.integradores.IntegracaoBotConsulta;
import br.com.fiap.bot.util.DataUtil;
import br.com.fiap.bot.util.MoedaUtil;

public class IntegracaoBotConsultaSaque extends IntegracaoBotConsulta {

	@Override
	public String integrarBanco(String resposta, Chat usuario) {
		BotComando botComando = new BotComando();
		TransacaoDetalhe transacaoDetalhe;
		StringBuilder retorno = new StringBuilder();
		
		try {
			transacaoDetalhe = botComando.listarRetiradas(usuario.id());
			retorno.append("EXTRATO DE SAQUES \n");
			for (Transacao transacao : transacaoDetalhe.getTransacoes()) {
				retorno.append(DataUtil.conveterDataPadraoBr(transacao.getDataHora())
						+ " - " + TipoTransacao.getTipoTransacao(transacao.getTipoTransacao()).toString()
						+ ": " + MoedaUtil.conveterMoedaBr(transacao.getValor()) + "\n");
			}
			retorno.append("Total: " + MoedaUtil.conveterMoedaBr(transacaoDetalhe.getSomatorio()));
		} catch (ContaInexistenteExcecao e) {
			retorno.append("Voc� ainda n�o tem uma conta, para criar sua conta digite /criar_conta"); 
		}		
		return retorno.toString();
	}

	@Override
	public String tratarPrimeiraInteracao(Chat usuario) {
		return "SAQUE";
	}

}
