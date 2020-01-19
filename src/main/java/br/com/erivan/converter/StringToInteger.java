package br.com.erivan.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToInteger implements Converter<String, Integer>{

	//para o campo numero do endereco, receber somente números
	
	@Override
	public Integer convert(String text) {
		text = text.trim();
		//verifico se é numero
		if(text.matches("[0-9]+")) {
			return Integer.valueOf(text);
		}
		return null;  //se for nullo a validacao do bean validation vai retornar uma menssagem
	}

}
