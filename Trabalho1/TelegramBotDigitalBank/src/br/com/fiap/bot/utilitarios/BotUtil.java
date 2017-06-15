package br.com.fiap.bot.utilitarios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.bot.contantes.EnumComandosBot;
import br.com.fiap.bot.contantes.EnumTipoIntegracaoBot;
import br.com.fiap.bot.validacao.IntegracaoBot;
import br.com.fiap.bot.validacao.IntegracaoBotAjuda;
import br.com.fiap.bot.validacao.IntegracaoBotConsultaDeposito;
import br.com.fiap.bot.validacao.IntegracaoBotConsultaExtrato;
import br.com.fiap.bot.validacao.IntegracaoBotConsultaSaque;
import br.com.fiap.bot.validacao.IntegracaoBotCriarConta;
import br.com.fiap.bot.validacao.IntegracaoBotExibirDados;
import br.com.fiap.bot.validacao.IntegracaoBotIncluirDependente;
import br.com.fiap.bot.validacao.IntegracaoBotModificarConta;
import br.com.fiap.bot.validacao.IntegracaoBotRealizarDeposito;
import br.com.fiap.bot.validacao.IntegracaoBotRealizarSaque;
import br.com.fiap.bot.validacao.IntegracaoBotSolicitacao;
import br.com.fiap.bot.validacao.IntegracaoBotStart;

public class BotUtil {

	private static final String TOKEN_ACESSO = "TOKEN";
	private static Map<Long,EnumComandosBot> ULTIMO_COMANDO_DO_USUARIO = new HashMap<>();
	
	/**
	 * M�todo respons�vel por realizar o start do bot e ficar buscando as mensagens enviadas
	 */
	public void executarBot(){
		TelegramBot bot = TelegramBotAdapter.build(TOKEN_ACESSO);		
		int m = 0;

		while (true) {
			List<Update> updates = bot.execute(new GetUpdates().limit(100).offset(m)).updates();
			
			for (Update update : updates) {
				m = update.updateId() + 1;
				
				bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				
				String mensagemRetorno = tratarMensagemBot(update.message().chat(), update.message().text());
				
				bot.execute(new SendMessage(update.message().chat().id(), mensagemRetorno.toString()));
			}
		}
	}

	/**
	 * M�todo respos�vel por entender a a��o solicitada pelo usu�rio e realizar as tratativas
	 * @param usuario
	 * @param mensagemRecebida
	 * @return Mensagem de resposta para o usu�rio
	 */
	private String tratarMensagemBot(Chat usuario, String mensagemRecebida) {
		StringBuffer mensagemRetorno = new StringBuffer();
		
		if (EnumComandosBot.START.getComando().equals(mensagemRecebida)) {
			
			IntegracaoBot resposta = BotUtil.definirClasseIntegracao(EnumComandosBot.LISTA_COMANDO_INTERACOES.get(mensagemRecebida));
			mensagemRetorno.append(resposta.exibeMensagemPrimeiraIntegracao().replace("?", usuario.firstName()));
			
			//Remove do historico caso existe, pois foi iniciado uma nova integracao
			removerHistoricoIntegracaoUsuario(usuario.id());
			
		} else if (EnumComandosBot.LISTA_COMANDO_INTERACOES.containsKey(mensagemRecebida)) {
			
			IntegracaoBot resposta = BotUtil.definirClasseIntegracao(EnumComandosBot.LISTA_COMANDO_INTERACOES.get(mensagemRecebida));
			mensagemRetorno.append(resposta.exibeMensagemPrimeiraIntegracao());
			
			if(EnumComandosBot.LISTA_COMANDO_INTERACOES.get(mensagemRecebida).getEnumTipoIntegracaoBot().equals(EnumTipoIntegracaoBot.SOLICITACAO)){
				//Adiciona no cache da aplica��o a �ltima intera��o realiza para tratamento posterior
				adicionarHistoricoIntegracaoUsuario(usuario.id(), EnumComandosBot.LISTA_COMANDO_INTERACOES.get(mensagemRecebida));
			}else{
				//Remove �ltima interacao realizada porque foi realizada uma intera��o do tipo Consulta 
				removerHistoricoIntegracaoUsuario(usuario.id());
			}
			
		} else if (ULTIMO_COMANDO_DO_USUARIO.containsKey(usuario.id())) {
			
			EnumComandosBot ultimoComandoExecutado = ULTIMO_COMANDO_DO_USUARIO.get(usuario.id());			
			IntegracaoBotSolicitacao resposta = (IntegracaoBotSolicitacao) BotUtil.definirClasseIntegracao(ultimoComandoExecutado);
			
			//Valida se a resposta est� correta, se estiver retorna e retira do historico
			if (resposta.validarResposta(mensagemRecebida, ultimoComandoExecutado)) {
				mensagemRetorno.append(resposta.integrarBanco(mensagemRecebida,	ultimoComandoExecutado));
				removerHistoricoIntegracaoUsuario(usuario.id());
			} else {
				mensagemRetorno.append(resposta.informarErroNaResposta());
			}
			
		} else {
			mensagemRetorno.append("Desculpe, n�o entendi... digite /ajuda para obter a lista de comandos conhecidos.");

		}
		
		return mensagemRetorno.toString();
	}
	
	/**
	 * M�todo respons�vel por definir qual ser� a classe de valida��o e integra��o conforme os comandos
	 * @param ultimoComandoExecutado
	 * @return
	 */
	private static IntegracaoBot definirClasseIntegracao(EnumComandosBot ultimoComandoExecutado) {
		IntegracaoBot retorno = null;
		
		switch (ultimoComandoExecutado.getComando()) {
		case "/start":
			retorno = new IntegracaoBotStart();
			break;
		case "/ajuda":
			retorno = new IntegracaoBotAjuda();
			break;			
		case "/criar_conta":
			retorno = new IntegracaoBotCriarConta();
			break;
		case "/modificar_conta":
			retorno = new IntegracaoBotModificarConta();
			break;
		case "/incluir_dependente":
			retorno = new IntegracaoBotIncluirDependente();
			break;
		case "/exibir_dados":
			retorno = new IntegracaoBotExibirDados();
			break;
		case "/realizar_deposito":
			retorno = new IntegracaoBotRealizarDeposito();
			break;
		case "/realizar_saque":
			retorno = new IntegracaoBotRealizarSaque();
			break;
		case "/consultar_extrato":
			retorno = new IntegracaoBotConsultaExtrato();
			break;
		case "/consultar_depositos":
			retorno = new IntegracaoBotConsultaDeposito();
			break;
		case "/consultar_saques":
			retorno = new IntegracaoBotConsultaSaque();
			break;
		default:
			break;
		}
		return retorno;
	}

	/**
	 * M�todo respons�vel por remover os comandos do hist�rico do usu�rio
	 * @param id de identifica��o do usu�rio
	 */
	private void removerHistoricoIntegracaoUsuario(Long id){
		ULTIMO_COMANDO_DO_USUARIO.remove(id);
	}
	
	/**
	 * M�todo respons�vel por adicionar ou atualizar o �ltimo comando executado pelo usu�rio
	 * @param id de identifica��o do usu�rio
	 * @param comando que ser� adicionado ou atualizado no hist�rico do usu�rio
	 */
	private void adicionarHistoricoIntegracaoUsuario(Long id, EnumComandosBot comando){
		ULTIMO_COMANDO_DO_USUARIO.put(id, comando);
	}
}
