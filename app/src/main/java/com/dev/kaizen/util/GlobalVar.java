package com.dev.kaizen.util;

import java.io.Serializable;

public class GlobalVar implements Serializable {
	private static final long serialVersionUID = 4209785791151665560L;
	
	private static GlobalVar instance;

	private String idToken;
	private String account;
	private String program;
	private String profile;
	private String grup;

	static {
		instance = new GlobalVar();
	}

	private GlobalVar() { }

	public static GlobalVar getInstance() {
		return GlobalVar.instance;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getGrup() {
		return grup;
	}

	public void setGrup(String grup) {
		this.grup = grup;
	}
}
