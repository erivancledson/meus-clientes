package br.com.erivan.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
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

import br.com.erivan.domain.Servico;
import br.com.erivan.repository.ServicoRepository;
import br.com.erivan.service.ServicoService;

@Controller
@RequestMapping("/servicos")
public class ServicoController {
	
	@Autowired
	ServicoService servicoService;
	
	@Autowired
	ServicoRepository servicoRepository;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Servico servico) {
		return "servico/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("servicos", servicoService.buscarTodos());
		return "servico/lista";
	}
	
	
	@PostMapping("/salvar")
	public String salvar(@Valid Servico servico, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			return "servico/cadastro";
		}
		
		try {
			servicoService.salvarServico(servico);
			attr.addFlashAttribute("success", "Serviço inserido com sucesso.");
			
		}catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("fail", "Ops... Serviço ja cadastrado.");
		
		}
	
		return "redirect:/servicos/cadastrar";
		
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("servico", servicoService.buscarPorId(id));
		return "servico/cadastro";
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/editar")
	public String editar(@Valid Servico servico, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			return "servico/cadastro";
		}
		servicoService.editarServico(servico);
		attr.addFlashAttribute("success", "Serviço editado com sucesso.");
		return "redirect:/servicos/listar";
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		if(servicoService.servicoTemPagamentos(id)) {
			attr.addFlashAttribute("fail", "Serviço não excluido. Tem pagamento(s) vinculado(s).");
		}else {
			servicoService.excluir(id);
			attr.addFlashAttribute("success", "Serviço excluido com sucesso.");
		}
		
		
		return "redirect:/servicos/listar";
	}
	
	
	@GetMapping("**/pesquisarServico")
	public ModelAndView pesquisarServico(@RequestParam("nome") String nome) {
		ModelAndView modelAndView = new ModelAndView("servico/lista");
		modelAndView.addObject("servicos", servicoRepository.findServicoByName(nome));
		return modelAndView;
	}
	

}
