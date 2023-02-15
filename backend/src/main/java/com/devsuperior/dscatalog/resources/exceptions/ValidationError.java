package com.devsuperior.dscatalog.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

//Classe que já tem tudo do standardError e mais um lista do FieldMessage
public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;
	
	
	//Não precisa de construtor porque a classe está herdando
	private List<FieldMessage> errors = new ArrayList<>();

	//Pega somente o getErrors
	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	//Método para passar como parametro o nome do campo e a mensagem e instanciar um novo FieldMessage
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
	
	
}
