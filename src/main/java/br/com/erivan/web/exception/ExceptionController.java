package br.com.erivan.web.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import br.com.erivan.exception.AcessoNegadoException;


@ControllerAdvice
public class ExceptionController {
	
	//excessao para quem tentar acessar uma consulta via url que não lhe pertence
	@ExceptionHandler(AcessoNegadoException.class)
	public ModelAndView acessoNegadoException(AcessoNegadoException ex) {
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", 403);
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", ex.getMessage());
		return model;
	}

}
