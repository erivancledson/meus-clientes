package br.com.erivan.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.erivan.domain.Orcamento;
import br.com.erivan.domain.StatusOrcamento;
import br.com.erivan.repository.OrcamentoRepository;
import br.com.erivan.service.OrcamentoService;


@Controller
@RequestMapping("/orcamentos")
public class OrcamentoController {
	
	@Autowired
	OrcamentoService orcamentoService;
	
	@Autowired
	OrcamentoRepository orcamentoRepository;
	
	@GetMapping("/novo/cadastro")
	public String cadastrar(Orcamento orcamento) {
		return "orcamento/cadastro";
	}
	
	@PostMapping("/cadastro/salvar")
	public String salvar(@Valid Orcamento orcamento, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors()){
			
			return "orcamento/cadastro";
			
		}
		
		orcamentoService.salvarOrcamento(orcamento);
		attr.addFlashAttribute("success", "Pedido de orçamento enviado com sucesso..");
		return "redirect:/orcamentos/novo/cadastro";
		
	
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("orcamentos", orcamentoService.buscarTodos());
		return "orcamento/lista";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("orcamento", orcamentoService.buscarPorid(id));
		return "orcamento/alterar";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/editar")
	public String editar(@Valid Orcamento orcamento, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors()) {
			
			return "orcamento/alterar";
		}
		
		orcamentoService.editarOrcamento(orcamento);
		attr.addFlashAttribute("success", "Orçamento atualizado com sucesso.");
		return "redirect:/orcamentos/listar";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		orcamentoService.excluir(id);
		attr.addFlashAttribute("success", "Orçamento excluido com sucesso.");
		
		return "redirect:/orcamentos/listar";
	}
	
	
	//pegar os status do orcamento
	@ModelAttribute("status")
	public StatusOrcamento[] getOrcamento() {
		return StatusOrcamento.values();
	}
	
	
	//pesquisar por  nome
	@GetMapping("**/pesquisarNomeOrcamento")
	public ModelAndView pesquisarNomeCliente(@RequestParam("nome") String nome) {
		ModelAndView modelAndView = new ModelAndView("orcamento/lista");
		modelAndView.addObject("orcamentos", orcamentoRepository.findOrcamentoByName(nome));
		return modelAndView;
	}

	//pesquisar por  data
	@GetMapping("**/pesquisarPorPeriodo")
	public ModelAndView pesquisarPorPeriodo(@RequestParam("dataInicio")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInicio , @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date dataFim) {
		ModelAndView modelAndView = new ModelAndView("orcamento/lista");
		modelAndView.addObject("orcamentos", orcamentoRepository.findOrcamentoByDate(dataInicio , dataFim));
		return modelAndView;
	}

	
	//pesquisar por status
	@GetMapping("**/pesquisarStatusOrcamento")
	public ModelAndView pesquisarstatus(@RequestParam("nome") StatusOrcamento nome) {
		ModelAndView modelAndView = new ModelAndView("orcamento/lista");
		modelAndView.addObject("orcamentos", orcamentoRepository.findOrcamentoByStatusOrcamento(nome));
		return modelAndView;
	}
}
