package br.com.erivan.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Chamado {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 255, min = 5)
	private String assunto;
	
	@NotBlank
	private String descricao;
	
	
	@ManyToOne
	@JoinColumn(name = "usuario_id_fk")
	private Usuario usuario;
	

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusChamado StatusChamado;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataChamado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public StatusChamado getStatusChamado() {
		return StatusChamado;
	}

	public void setStatusChamado(StatusChamado statusChamado) {
		StatusChamado = statusChamado;
	}

	public Date getDataChamado() {
		return dataChamado;
	}

	public void setDataChamado(Date dataChamado) {
		this.dataChamado = dataChamado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	

}
