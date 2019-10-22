package br.com.erivan.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import br.com.erivan.domain.Cliente;
import br.com.erivan.domain.UF;
import br.com.erivan.repository.ClienteRepository;
import br.com.erivan.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	ClienteService clienteService;
	//para o pesquisar por nome
	@Autowired
	ClienteRepository clienteRepository;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Cliente cliente) {
		return "cliente/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("clientes", clienteService.buscarTodos());
		return "cliente/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			return "cliente/cadastro";
		}
		
		try {
			
			clienteService.salvarCliente(cliente);
			attr.addFlashAttribute("success", "Cliente inserido com sucesso.");
			
		}catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("fail", "Ops... Cliente ja cadastrado.");
		
		}
		
		return "redirect:/clientes/cadastrar";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("cliente", clienteService.buscarPorId(id));
		return "cliente/cadastro";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/editar")
	public String editar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors()) {
			return "cliente/cadastro";
		}
		
		attr.addFlashAttribute("success", "Cliente editado com sucesso");
	   clienteService.editarCliente(cliente);
	   return "redirect:/clientes/listar";
	}
	

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		if(clienteService.clienteTemPagamentos(id)) {
			attr.addFlashAttribute("fail", "Cliente não excluido. Tem pagamento(s) vinculado(s).");
		}else {
			clienteService.excluir(id);
			attr.addFlashAttribute("success", "Cliente excluido com sucesso");
		}
		
		return "redirect:/clientes/listar";
	}
	
	//lista de ufs enum
	@ModelAttribute("ufs")
	public UF[] getUFs() {
		return UF.values();
	}
	
	//pesquisa pelo nome do cliente
	@GetMapping("**/pesquisarCliente")
	public ModelAndView pesquisarCliente(@RequestParam("nome") String nome) {
		ModelAndView modelAndView = new ModelAndView("cliente/lista");
		modelAndView.addObject("clientes", clienteRepository.findClienteByName(nome));
		return modelAndView;
	}

}
