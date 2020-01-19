package br.com.erivan.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import br.com.erivan.domain.Servico;
import br.com.erivan.service.ServicoService;

@Component
public class StringToServicoConversor implements Converter<String, Servico>{
	//converte de string para objeto servico que é o que vem do select de serviço em pagamento
	@Autowired
	private ServicoService servicoService;
	
	@Override
	public Servico convert(String text) {
		if(text.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(text);
		return servicoService.buscarPorId(id);
	}

}
