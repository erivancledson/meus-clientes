package br.com.erivan.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.erivan.domain.Pagamento;
import br.com.erivan.domain.Servico;
import br.com.erivan.domain.StatusPagamento;
import br.com.erivan.repository.PagamentoRepository;
import br.com.erivan.service.ClienteService;
import br.com.erivan.service.PagamentoService;
import br.com.erivan.service.ServicoService;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {
	
	@Autowired
	private PagamentoService pagamentoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ServicoService ServicoService;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Pagamento pagamento) {
		return "pagamento/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("pagamentos", pagamentoService.buscarTodos());
		return "pagamento/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Pagamento pagamento, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors())
			return "pagamento/cadastro";
		
		pagamentoService.salvarPagamento(pagamento);
		attr.addFlashAttribute("success", "Pagamento inserido com sucesso.");
		return "redirect:/pagamentos/cadastrar";
	}
	@PreAuthorize("hasAnyAuthority('ADMIN', 'FINANCEIRO')")
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("pagamento", pagamentoService.buscarPorId(id));
		return "pagamento/cadastro";
	}
	@PreAuthorize("hasAnyAuthority('ADMIN', 'FINANCEIRO')")
	@PostMapping("/editar")
	public String editar(@Valid Pagamento pagamento, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors()) {
			return "pagamento/cadastro";
		}
		
		pagamentoService.editarPagamento(pagamento);
		attr.addFlashAttribute("success", "Pagamento editado com sucesso.");
		return "redirect:/pagamentos/listar";
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		pagamentoService.excluir(id);
		attr.addFlashAttribute("success", "Pagamento removido com sucesso.");
		return "redirect:/pagamentos/listar";
	}
	
	//chave para o combo box
	@ModelAttribute("clientes")
	public List<Cliente> getClientes(){
		return clienteService.buscarTodos();
	}
	//chave para o combo box
	@ModelAttribute("servicos")
	public List<Servico> getServico(){
		return ServicoService.buscarTodos();
	}
	//status do pagamento lista
	@ModelAttribute("status")
	public StatusPagamento[] getPagamento() {
		return  StatusPagamento.values();
	}
	
	@GetMapping("**/pesquisarTitulo")
	public ModelAndView pesquisarPagamentoTitulo(@RequestParam("titulo") String titulo) {
		ModelAndView modelAndView = new ModelAndView("pagamento/lista");
		modelAndView.addObject("pagamentos", pagamentoRepository.findByTitulo(titulo));
		return modelAndView;
	}
	
	@GetMapping("**/pesquisarPorCliente")
	public ModelAndView pesquisarPagamentoCliente(@RequestParam("cliente") Long id) { //recebe o objeto e pega o id do mesmo
		ModelAndView modelAndView = new ModelAndView("pagamento/lista");
		modelAndView.addObject("pagamentos", pagamentoRepository.buscarPorCliente(id));
		return modelAndView;
	}
	
	
	@GetMapping("**/pesquisarPorServico")
	public ModelAndView pesquisarPagamentoServico(@RequestParam("servico") Long id) {
		ModelAndView modelAndView = new ModelAndView("pagamento/lista");
		modelAndView.addObject("pagamentos", pagamentoRepository.buscarPorServico(id));
		return modelAndView;
	}
	
	

	
  
}
