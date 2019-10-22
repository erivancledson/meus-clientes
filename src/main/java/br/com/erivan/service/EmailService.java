package br.com.erivan.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailService {

	
	// link: https://www.hostinger.com.br/tutoriais/aprenda-a-utilizar-o-smtp-google/
	
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SpringTemplateEngine template;
	
	public void enviarPedidoDeConfirmacaoDeCadastro(String destino, String codigo) throws MessagingException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8"); //informando que vai ser enviado imagens, textos, arquivos html e etc
	//mensagem do email
	Context context = new Context();
	context.setVariable("titulo", "Bem vindo ao sistema de suporte");
	context.setVariable("texto", "Precisamos que confirme seu cadastro, clicando no link abaixo");
	context.setVariable("linkConfirmacao", "http://localhost:9000/usuarios/confirmacao/cadastro?codigo=" + codigo); //link para confirmacao do cadastro
	
	//pega o html de email/confirmacao e passa as variaveis do context para ela
	String html = template.process("email/confirmacao", context);
	helper.setTo(destino);
	helper.setText(html, true);
	helper.setSubject("Confirmação de Cadastro");
	helper.setFrom("nao-responder@erivancosta.com.br"); //nao é o email de envio mas é o email que será exibido, muitas empresas fazem isso
	helper.addInline("logo", new ClassPathResource("/static/image/clientes-png.png")); //caminho da imagem, é a ultima instrução que se coloca no helper
	
	mailSender.send(message);
	
	}
	
	public void enviarPedidoRedefinicaoSenha(String destino, String verificador) throws MessagingException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8"); //informando que vai ser enviado imagens, textos, arquivos html e etc
	//mensagem do emails
	Context context = new Context();
	context.setVariable("titulo", "Redefinição de senha");
	context.setVariable("texto", "Para redefinir sua senha use o código de verificação quando exigido no formulário");
	context.setVariable("verificador", verificador);
	
	
	String html = template.process("email/confirmacao", context);
	helper.setTo(destino);
	helper.setText(html, true);
	helper.setSubject("Redefinição de senha");
	helper.setFrom("no-replay@erivancosta.com.br"); //nao é o email de envio mas é o email que será exibido, muitas empresas fazem isso
	helper.addInline("logo", new ClassPathResource("/static/image/clientes-png.png")); //caminho da imagem, é a ultima instrução que se coloca no helper
	
	mailSender.send(message);
	
	}
	
	
}
