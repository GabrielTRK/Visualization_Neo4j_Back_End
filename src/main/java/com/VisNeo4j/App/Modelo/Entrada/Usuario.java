package com.VisNeo4j.App.Modelo.Entrada;

import java.util.List;

public class Usuario {
	
	private String usuario;
	private String pass;
	
	public Usuario(String usuario, String pass) {
		super();
		this.usuario = usuario;
		this.pass = pass;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	@Override
	public String toString() {
		return "Usuario [usuario=" + usuario + ", pass=" + pass + "]";
	}
	
	
	

}
