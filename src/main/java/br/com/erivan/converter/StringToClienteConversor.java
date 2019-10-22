package br.com.erivan.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.erivan.domain.Cliente;
import br.com.erivan.service.ClienteService;

@Component
public class StringToClienteConversor implements Converter<String, Cliente>{
	//converte de string para objeto cliente que é o que vem do select de cliente em pagamento
	@Autowired
	private ClienteService clienteService;
	
	@Override
	public Cliente convert(String text) {
		if(text.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(text);
		return clienteService.buscarPorId(id);
	}

}
