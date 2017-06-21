package br.com.fiap.bot.util;

import java.util.ArrayList;
import java.util.List;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;

import br.com.fiap.bot.Bot;
import br.com.fiap.bot.constantes.ConstantesBot;
import br.com.fiap.bot.constantes.EnumComandosBot;
import br.com.fiap.bot.dao.impl.MensagemIntegracaoDao;
import br.com.fiap.bot.entidades.MensagemIntegracao;
import br.com.fiap.bot.integradores.IntegracaoBot;
import br.com.fiap.bot.integradores.impl.IntegracaoBotAjuda;
import br.com.fiap.bot.integradores.impl.IntegracaoBotConsultaDeposito;
import br.com.fiap.bot.integradores.impl.IntegracaoBotConsultaEmprestimo;
import br.com.fiap.bot.integradores.impl.IntegracaoBotConsultaExtrato;
import br.com.fiap.bot.integradores.impl.IntegracaoBotConsultaSaque;
import br.com.fiap.bot.integradores.impl.IntegracaoBotConsultaTarifaPaga;
import br.com.fiap.bot.integradores.impl.IntegracaoBotCriarConta;
import br.com.fiap.bot.integradores.impl.IntegracaoBotExibirDados;
import br.com.fiap.bot.integradores.impl.IntegracaoBotIncluirDependente;
import br.com.fiap.bot.integradores.impl.IntegracaoBotModificarConta;
import br.com.fiap.bot.integradores.impl.IntegracaoBotRealizarDeposito;
import br.com.fiap.bot.integradores.impl.IntegracaoBotRealizarSaque;
import br.com.fiap.bot.integradores.impl.IntegracaoBotSolicitarEmprestimo;
import br.com.fiap.bot.integradores.impl.IntegracaoBotStart;

public class BotUtil {

	public static String retirarComandoDaMensagem(String mensagemRecebida, String comando) {
		return mensagemRecebida.replaceAll(comando, "");
	}

	/**
	 * Encontra todos os comandos conhecidos pelo bot enviados pelo usu�rio
	 * @param mensagemRecebida Mensagem enviada pelo usu�rio
	 * @return List<String> com os comandos encontrados
	 */
	public static List<String> encontrarComandosNaMensagem(String mensagemRecebida) {
		String [] palavras = mensagemRecebida.split(" ");
		List<String> comandosEncontratos = new ArrayList<>(); 
		for (int i = 0; i < palavras.length; i++) {
			if(palavras[i].startsWith("/") && EnumComandosBot.LISTA_COMANDO_INTERACOES.containsKey(palavras[i].trim())){
				comandosEncontratos.add(palavras[i].trim());
			}
		}
		return comandosEncontratos;
	}

	/**
	 * Valida ser a mensagemRecebida � apenas o comando solicita (ex: /criar_conta) 
	 * @param mensagemRecebida Mensagem enviada pelo usu�rio no telegram
	 * @return true - Mensagem com um comando simples | false - Mensagem provavelmente cont�m o comando e o complemento referente o comando
	 */
	public static boolean isComandoSimples(String mensagemRecebida) {
		return EnumComandosBot.LISTA_COMANDO_INTERACOES.containsKey(mensagemRecebida);
	}

	/**
	 * Valida se o bot esta aguardando uma resposta do usu�rio 
	 * @param usuario Usuario do Telegram
	 * @return true - Bot aguardando resposta do Usuario | false - Bot n�o esta aguardando mensagem de resposta
	 */
	public static boolean botAguardandoRespostaDoUsuario(Chat usuario) {
		return Bot.ULTIMO_COMANDO_DO_USUARIO.containsKey(usuario.id());
	}

	/**
	 * V�lida se no comando enviado cont�m uma entidade do tipo bot_command 
	 * @param message Objeto Message do Telegram
	 * @return <code>true</code> se na mensagem cont�m pelo menos um comando, se n�o <code>false</code>
	 */
	public static boolean isMensagemComComando(Message message) {
		boolean mensagemComComando = false;
		
		if(message.entities() != null){
			for (int i = 0; i < message.entities().length; i++) {
				if(message.entities()[i].type().name().equals(ConstantesBot.BOT_COMMAND)){
					mensagemComComando = true;
					break;
				}
			}
		}
		
		return mensagemComComando;
	}

	/**
	 * M�todo respons�vel por definir qual ser� a classe de valida��o e integra��o conforme os comandos
	 * @param EnumComandosBot referente o comando executado
	 * @return Inst�ncia da classe de acordo com o comando executado
	 */
	public static IntegracaoBot definirClasseIntegracao(EnumComandosBot ultimoComandoExecutado) {
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
		case "/solicitar_emprestimo":
			retorno = new IntegracaoBotSolicitarEmprestimo();
			break;
		case "/consultar_tarifas_pagas":
			retorno = new IntegracaoBotConsultaTarifaPaga();
			break;
		case "/consultar_emprestimo":
			retorno = new IntegracaoBotConsultaEmprestimo();
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
	public static void removerHistoricoIntegracaoUsuario(Long id){
		Bot.ULTIMO_COMANDO_DO_USUARIO.remove(id);
	}
	
	/**
	 * M�todo respons�vel por adicionar ou atualizar o �ltimo comando executado pelo usu�rio
	 * @param id de identifica��o do usu�rio
	 * @param comando que ser� adicionado ou atualizado no hist�rico do usu�rio
	 */
	public static void adicionarHistoricoIntegracaoUsuario(Long id, EnumComandosBot comando){
		Bot.ULTIMO_COMANDO_DO_USUARIO.put(id, comando);
	}
	
	/**
	 * Metodo respons�vel por gravar na base todas as intera�oes do usu�rio
	 * @param usuario informa��es usuario telegram
	 * @param mensagemRecebida do usuario
	 * @param mensagemEnviada para o usuario
	 */	
	public static void salvarMensagens(Chat usuario, String mensagemRecebida, String mensagemEnviada ){
		MensagemIntegracao mensagemIntegracao = new MensagemIntegracao();
		MensagemIntegracaoDao mensagemIntegracaoDao = new MensagemIntegracaoDao();
		
		mensagemIntegracao.setIdTelegram(usuario.id());
		mensagemIntegracao.setNomeTelegram(usuario.firstName());
		mensagemIntegracao.setMensagemRecebida(mensagemRecebida);
		mensagemIntegracao.setMensagemEnviada(mensagemEnviada);	
		
		mensagemIntegracaoDao.adicionar(mensagemIntegracao);
		mensagemIntegracaoDao.close();		
		
	}
}
