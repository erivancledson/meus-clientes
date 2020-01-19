package br.com.erivan.domain;

public enum PerfilTipo {
	
ADMIN(1, "ADMIN"), ATENDENTE(2, "ATENDIMENTO"), FINANCEIRO(3, "FINANCEIRO"), CLIENTE(4, "CLIENTE");
	
	private long cod;
	private String desc;

	private PerfilTipo(long cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}

	public long getCod() {
		return cod;
	}

	public String getDesc() {
		return desc;
	}

}
