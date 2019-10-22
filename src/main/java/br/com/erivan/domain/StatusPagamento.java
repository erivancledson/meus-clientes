package br.com.erivan.domain;

public enum StatusPagamento {
	
	AGUARDANDO("AGUARDANDO"),
	PAGOU("PAGOU"),
	EM_ATRASOU("EM ATRASO"),
	NAO_PAGOU("NÃO PAGOU");
	
	private String nome;
	
	StatusPagamento(String nome){
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

}
