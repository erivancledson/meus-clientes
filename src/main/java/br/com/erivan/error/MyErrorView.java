package br.com.erivan.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

//customiza a pagina de error.html 
@Component
public class MyErrorView implements ErrorViewResolver {
	
	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> map) {
		
		ModelAndView model = new ModelAndView("error"); //pagina de error.html
		model.addObject("status", status.value());  //recupera o status referente ao error e adicona as variaveis
		switch (status.value()) {
				case 404:
					model.addObject("error", "Página não encontrada.");
					model.addObject("message", "A url para a página '" + map.get("path") + "' não existe.");
					break;
				case 500:
					model.addObject("error", "Ocorreu um erro interno no servidor.");
					model.addObject("message", "Ocorreu um erro inexperado, tente mais tarde.");
					break;
				default:
					model.addObject("error", map.get("error"));
					model.addObject("message", map.get("message"));
					break;
		}		
		return model;
	}

}
