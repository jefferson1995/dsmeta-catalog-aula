package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice  // esta anotação que agora faz o tratamento do erro
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class) //Para que ele saiba o tipo de exception que vai interroper
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now()); // pega o exato momento.
		err.setStatus(status.value()); // pega o status 400
		err.setError(" Resource not found"); // informa o tipo de erro
		err.setMessage(e.getMessage()); //Pega a mensagem do nome do erro que está na classe EntityFoundException
		err.setPath(request.getRequestURI()); // pega O CAMINHO DA URL SOLICITADA
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class) //Para que ele saiba o tipo de exception que vai interroper
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now()); 
		err.setStatus(status.value());       // pega o codigo de erro de requisição
		err.setError(" Darabase exception"); 
		err.setMessage(e.getMessage());       //Pega a mensagem do nome do erro que está na classe EntityFoundException
		err.setPath(request.getRequestURI()); 
		
		return ResponseEntity.status(status).body(err);
	}
}
