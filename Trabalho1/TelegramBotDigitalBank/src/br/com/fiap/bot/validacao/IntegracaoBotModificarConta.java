package br.com.fiap.bot.validacao;

import br.com.fiap.bot.contantes.EnumComandosBot;

public class IntegracaoBotModificarConta extends IntegracaoBotSolicitacao {

	public IntegracaoBotModificarConta() {
		super("Me fale os seus novos dados para que possamos atualizar. Por favor, informe nesta padr�o: cpf - email (Ex: 36521563511 - joao@email.com)", "cpf - email (Ex: 36521563511 - joao@email.com)");
	}

	@Override
	public Boolean validarResposta(String resposta, EnumComandosBot comandoBot) {
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
	public String integrarBanco(String resposta, EnumComandosBot comandoBot) {
		return "Parab�ns! Conta modificada com sucesso!";
	}

}
