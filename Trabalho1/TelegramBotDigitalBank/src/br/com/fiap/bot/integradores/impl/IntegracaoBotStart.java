package br.com.fiap.bot.integradores.impl;

import com.pengrad.telegrambot.model.Chat;

import br.com.fiap.bot.integradores.IntegracaoBotConsulta;

public class IntegracaoBotStart extends IntegracaoBotConsulta {

	@Override
	public String integrarBanco(String resposta, Chat usuario) {
		return "";
	}

	@Override
	public String tratarPrimeiraInteracao(Chat usuario) {
		return "Ol� "+usuario.firstName()+"!\nMeu nome � Pedrite, sou um atendente virtual e irei ajudar voc�.\nPara saber o que voc� pode fazer por aqui, digite /ajuda";
	}

}
