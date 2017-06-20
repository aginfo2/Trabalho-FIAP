package br.com.fiap.bot.integradores.impl;

import com.pengrad.telegrambot.model.Chat;

import br.com.fiap.banco.comandos.BotComando;
import br.com.fiap.banco.excecao.ContaInexistenteExcecao;
import br.com.fiap.bot.integradores.IntegracaoBotSolicitacao;

public class IntegracaoBotModificarConta extends IntegracaoBotSolicitacao {

	public IntegracaoBotModificarConta() {
		super("Me fale os seus novos dados para que possamos atualizar. Por favor, informe nesta padr�o: cpf - email (Ex: 36521563511 - joao@email.com)", "cpf - email (Ex: 36521563511 - joao@email.com)");
	}

	@Override
	public Boolean validarResposta(String resposta) {
		boolean respostaOk = true;		
		resposta = resposta.trim();
		String [] respostas = resposta.split("-");
		
		if(respostas.length != 2){
			respostaOk = false;
		}else{
			for (int i = 0; i < respostas.length; i++) {
				if(respostas[i].trim().length() == 0){
					respostaOk = false;
					break;
				}
			}
		}		
		
		return respostaOk;
	}

	@Override
	public String integrarBanco(String resposta, Chat usuario) {
		String retorno = "";
		BotComando botComando = new BotComando();
		try {
			botComando.modificarConta(1234, "98765432100", "novoteste@teste.com.br");
			retorno = "Parab�ns! Conta modificada com sucesso!";
		} catch (ContaInexistenteExcecao e) {
			retorno = "Voc� ainda n�o tem uma conta, para criar sua conta digite /criar_conta";
		}
		return retorno;
	}

}
