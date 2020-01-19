package br.com.erivan.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@SuppressWarnings("serial")
@Entity
@Table(name = "CLIENTES")
public class Cliente implements Serializable{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	@NotBlank
	@Size(max = 255, min = 5)
	@Column(nullable = false, unique = true)
	private String nome;
	
	@NotNull()
	@Size(max = 11, min = 11)
	@Column(nullable = false)
	private String telefone;
	
	@NotBlank
	@Size(max = 255)
	@Column(nullable = false, unique = true)
	private String email;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL) //quando inserir o cliente ja insere o endereco e se for deletado mesmo jeito
	@JoinColumn(name = "endereco_id_fk")
	private Endereco endereco;
	
	@OneToMany(mappedBy = "cliente")
	private List<Pagamento> pagamentos;
	

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}



	
}
