package br.com.erivan.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;




@SuppressWarnings("serial")
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	@NotBlank
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@NotBlank
	@Column(name = "senha", nullable = false)
	private String senha;
	
	//id do perfil que cada usuario vai ter
		@ManyToMany //1 usuario tem varios perfis e varios perfis para varios usuarios
		@JoinTable(
			name = "usuarios_tem_perfis",   //cria uma nova tabela
	        joinColumns = { @JoinColumn(name = "usuario_id", referencedColumnName = "id") }, //que contem o id do usuario e o id do perfil
	        inverseJoinColumns = { @JoinColumn(name = "perfil_id", referencedColumnName = "id") }
		)
		private List<Perfil> perfis;

	
	@Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean ativo;
	
	@Column(name = "codigo_verificador", length = 6)
	private String codigoVerificador;
	
	
	public Usuario() {
		super();
	}
	
	//construtor
	public Usuario(String email) {
		this.email = email;
	}
	//usuario cliente faz chamado
	@OneToMany(mappedBy = "usuario")
	private List<Chamado> chamados;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	// adiciona perfis a lista
	public void addPerfil(PerfilTipo tipo) {
		if (this.perfis == null) {
			this.perfis = new ArrayList<>();
		}
		this.perfis.add(new Perfil(tipo.getCod())); //passa o codigo do perfil
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	
	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public boolean hasNotId() {
		return id == null;
	}

	public boolean hasId() {
		return id != null;
	}

	public String getCodigoVerificador() {
		return codigoVerificador;
	}

	public void setCodigoVerificador(String codigoVerificador) {
		this.codigoVerificador = codigoVerificador;
	}

	public List<Chamado> getChamados() {
		return chamados;
	}

	public void setChamados(List<Chamado> chamados) {
		this.chamados = chamados;
	}
	
	

}
