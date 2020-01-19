package br.com.erivan.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.erivan.domain.Chamado;
import br.com.erivan.domain.StatusChamado;
import br.com.erivan.domain.Usuario;
import br.com.erivan.repository.ChamadoRepository;
import br.com.erivan.service.ChamadoService;
import br.com.erivan.service.UsuarioService;

@Controller
@RequestMapping("/chamados")
public class ChamadoController {
	
	@Autowired
	ChamadoService chamadoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	ChamadoRepository chamadoRepository;

	@GetMapping({"/cadastrar"})
	public String cadastrarChamado(Chamado chamado) {
		return "chamado/cadastro";
	}

	//@AuthenticationPrincipal pega o usuario logado na sessão
	@PostMapping({"/salvar"})
	public String salvar(@Valid Chamado chamado, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		//pega o id do usuario
		Usuario usuario = usuarioService.buscarPorUsuarioEmail(user.getUsername()); //captura o usuario atraves do login
		chamado.setUsuario(usuario);
		chamadoService.salvarChamado(chamado);
		attr.addFlashAttribute("success", "Chamado enviado com sucesso, em até um dia útil vamos te responder");
		return "redirect:/chamados/cadastrar";
	}
	
	
	//buscar todos os chamados
	@PreAuthorize("hasAnyAuthority('ADMIN', 'ATENDENTE')")
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("chamados", chamadoService.buscarTodos());
		return "chamado/lista";
	}
	
	//chamados do cliente especifico

	@GetMapping("/listarMeusChamados")
	public ModelAndView listarMeusChamados(ModelMap model, @AuthenticationPrincipal() Usuario usuario, String email) {
		
		List<Chamado> chamados = chamadoService.findByUsuarioId(usuario.getId());
		model.addAttribute("chamados", chamados);
		
		return new ModelAndView("chamado/meus-chamados", model);
		
	}
	
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'ATENDENTE')")
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("chamado", chamadoService.buscarPorId(id));
		return "chamado/alterar";
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'ATENDENTE')")
	@PostMapping("/editar")
	public String editar(@Valid Chamado chamado, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors()) {
			 return "chamado/alterar";
		}
		
	    chamadoService.editarChamado(chamado);
		attr.addFlashAttribute("success", "Status do chamado atualizado com sucesso.");
		return "redirect:/chamados/listar";
	}
	
	
	
	//status do pagamento lista
		@ModelAttribute("status")
		public StatusChamado[] getChamado() {
			return  StatusChamado.values();
		}
	
	
}
