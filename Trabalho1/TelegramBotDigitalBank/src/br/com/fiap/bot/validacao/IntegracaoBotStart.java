package br.com.fiap.bot.validacao;

import br.com.fiap.bot.constantes.EnumComandosBot;

public class IntegracaoBotStart extends IntegracaoBotConsulta {

	@Override
	public String integrarBanco(String resposta, EnumComandosBot comandoBot) {
		return "";
	}

	@Override
	public String exibeMensagemPrimeiraIntegracao() {
		return "Ol� ?!\nMeu nome � Pedrite, sou um atendente virtual e irei ajudar voc�.\nPara saber o que voc� pode fazer por aqui, digite /ajuda";
	}

}
