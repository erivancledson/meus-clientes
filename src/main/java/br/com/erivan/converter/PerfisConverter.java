package br.com.erivan.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import br.com.erivan.domain.Perfil;

@Component
public class PerfisConverter implements Converter<String[], List<Perfil>> {

	@Override
	public List<Perfil> convert(String[] source) {
		List<Perfil> perfis = new ArrayList<>();
		for(String id : source) {
			
		if(!id.equals("0")) {
			perfis.add(new Perfil(Long.parseLong(id))); //quando for diferente de 0 , ele salva mais de dois perfis
		}
				
			
			
		}
		return perfis;
	}


}
