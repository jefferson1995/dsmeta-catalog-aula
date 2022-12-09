package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@ControllerAdvice  // esta anotação que agora faz o tratamento do erro
public class ResourceExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class) //Para que ele saiba o tipo de exception que vai interroper
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now()); // pega o exato momento.
		err.setStatus(HttpStatus.NOT_FOUND.value()); // pega o status 400
		err.setError(" Resource not found"); // informa o tipo de erro
		err.setMessage(e.getMessage()); //Pega a mensagem do nome do erro que está na classe EntityFoundException
		err.setPath(request.getRequestURI()); // pega O CAMINHO DA URL SOLICITADA
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
}
