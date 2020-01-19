package br.com.erivan.domain;

public enum StatusOrcamento {
	
	AGUARDANDO_ANALISE("AGUARDANDO ANÁLISE"),
	PROPOSTA_ENVIADA("PROPOSTA ENVIADA"),
	FECHADO("FECHADO"),
	NAO_FECHADO("NÃO FECHADO");
	
	private String nome;
	

	StatusOrcamento(String nome){
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

}
