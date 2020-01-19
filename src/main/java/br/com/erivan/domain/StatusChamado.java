package br.com.erivan.domain;

public enum StatusChamado {
	
	ABERTO ("EM ABERTO"),
	FINALIZADO ("FINALIZADO");
	
	private String nome;
	
	StatusChamado(String nome){
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
