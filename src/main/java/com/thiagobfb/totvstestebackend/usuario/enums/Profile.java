package com.thiagobfb.totvstestebackend.usuario.enums;

public enum Profile {

	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private Profile(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	private int cod;
	private String descricao;
	
	
	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static Profile toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (Profile x : Profile.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
