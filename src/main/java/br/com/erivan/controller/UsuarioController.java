package br.com.erivan.controller;



import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.erivan.domain.Usuario;
import br.com.erivan.repository.UsuarioRepository;
import br.com.erivan.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	@Autowired
	private UsuarioRepository repository;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Usuario usuario) {
		return "usuario/cadastro";
	}
	
	//salva cadastro pelo admin
	@PostMapping("/salvar")		
	public String salvarUsuarios(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			return "usuario/cadastro";
		}
		
		try {
			
			service.salvarUsuario(usuario);
			attr.addFlashAttribute("success", "Usuário inserido com sucesso.");
			
		}catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("fail", "Ops... Usuário ja cadastrado.");
		
		}
		
		
		return "redirect:/usuarios/cadastrar";
	}
	
	
	//listar usuarios
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("usuarios", service.buscarTodos());
		return "usuario/lista";
	}
	
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.excluir(id);
		attr.addFlashAttribute("success", "Usuário removido com sucesso.");
		return "redirect:/usuarios/listar";
	}
	
	
		//pega os dados do usuario e envia para a tela de cadastro do usuario
			@GetMapping("/editar/{id}")
			public String preEditarCredenciais(@PathVariable("id") Long id, ModelMap model) {
				model.addAttribute("usuario", service.buscarPorId(id));
				return "usuario/cadastro";
			}
			
			@PostMapping("/editar")
			public String editar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attr) {
				if(result.hasErrors()) {
					return "usuario/cadastro";
				}
				
				service.editarUsuario(usuario);
				attr.addFlashAttribute("success", "Usuário editado com sucesso.");
				return "redirect:/usuarios/listar";
			}
			
			//pesquisar pelo email
			@GetMapping("**/pesquisarEmail")
			public ModelAndView pesquisarUsuario(@RequestParam("email") String email) {
				ModelAndView modelAndView = new ModelAndView("usuario/lista");
				modelAndView.addObject("usuarios", repository.findByEmail(email));
				return modelAndView;
			}
			
			
			   //abrir página de novo  cadastro de cliente
		    @GetMapping("/novo/cadastro")
		    public String novoCadastro(Usuario usuario) {
		    	return "cadastrar-se";
		    }
		    
		    //pagina de resposta do cadastro de paciente
		    @GetMapping("/cadastro/realizado")
		    public String cadastroRealizado() {
		    	return "cadastro-realizado";
		    }
		    
		    //salva o cliente
		    @PostMapping("/cadastro/cliente/salvar")
		    public String salvarCadastroCliente(Usuario usuario, BindingResult result) throws MessagingException{
		    	try {
		    		service.salvarCadastroCliente(usuario);
		    	}catch(DataIntegrityViolationException e){
		    		result.reject("email", "Ops... Este e-mail já existe na base de dados.");
		    		return "cadastrar-se";
		    	}
		    	
		    	return "redirect:/usuarios/cadastro/realizado";
		    }
		    
		    
		    //recebe a requisicao de confirmacao de cadastro
		    @GetMapping("/confirmacao/cadastro")
		    public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr) {
		    	
		    	service.ativarCadastroCliente(codigo);
		    	attr.addFlashAttribute("alerta", "sucesso");
		    	attr.addFlashAttribute("titulo", "Cadastro Ativado");
		    	attr.addFlashAttribute("texto", "Parabéns, seu cadastro está ativo.");
		    	attr.addFlashAttribute("subtexto", "Siga com o seu login/senha");
		    	return "redirect:/login";
		    }
		   
		    //abre a pagina de pedido de redefinicao de senha
		    @GetMapping("/p/redefinir/senha")
		    public String pedidoRedefinirSenha() {
		    	return "usuario/pedido-recuperar-senha";
		    }
		    
		    //envia o email
		    @GetMapping("/p/recuperar/senha")
		    public String redefinirSenha(String email, ModelMap model) throws MessagingException {
		    	service.pedidoRedefinicaoDeSenha(email);
		    	model.addAttribute("success", "Em instantes você receberá um e-mail para prosseguir "
		    			+ "com a redefinição de sua senha.");
		    	model.addAttribute("usuario", new Usuario(email));
		    	
		    	return "usuario/recuperar-senha";
		    }
		    
		    //salvar a nova senha via recuperacao de senha
		    @PostMapping("/p/nova/senha")
		    public String confirmacaoDeRedefinicaoDeSenha(Usuario usuario, ModelMap model) {
		    	Usuario u = service.buscarPorEmail(usuario.getEmail());
		    	//compara os codigo verificadores se eles são diferentes
		    	if(!usuario.getCodigoVerificador().equals(u.getCodigoVerificador())) {
		    		model.addAttribute("fail", "Código verificador não confere");
		    		return "usuario/recuperar-senha";
		    	}
		    	
		    	u.setCodigoVerificador(null); //limpa o codigo verificador do banco de dados
		    	service.alterarSenha(u, usuario.getSenha());  //aqui altera a senha
		    	model.addAttribute("alerta", "sucesso");
		    	model.addAttribute("titulo", "senha redefinida!");
		    	model.addAttribute("texto", "Você já pode logar no sistema");
		    	return "login";
		    	
		    }
		    
		    //editar senha dentro do painel
		    @GetMapping("/editar/senha")
		    public String abrirEditarSenha() {
		    	return "usuario/editar-senha";
		    }
		    
		    @PreAuthorize("hasAnyAuthority('ADMIN', 'FINANCEIRO', 'CLIENTE')")
		    @PostMapping("/confirmar/senha")
		    public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2, 
		    						  @RequestParam("senha3") String s3, @AuthenticationPrincipal User user,
		    						  RedirectAttributes attr) {
		    	// se senha 1 for diferente da senha 2
		    	if (!s1.equals(s2)) {
		    		attr.addFlashAttribute("fail", "Senhas não conferem, tente novamente");
		    		return "redirect:/usuarios/editar/senha";
		    	}
		    	
		    	Usuario u = service.buscarPorEmail(user.getUsername()); //busca do usuario do banco de dados
		    	if(!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
		    		attr.addFlashAttribute("fail", "Senha atual não confere, tente novamente");
		    		return "redirect:/usuarios/editar/senha";
		    	}
		    		//objeto usuario e a nova senha digitada
		    	service.alterarSenha(u, s1);
		    	attr.addFlashAttribute("success", "Senha alterada com sucesso.");
		    	return "redirect:/usuarios/editar/senha";
		    }
		    
		
	
}
