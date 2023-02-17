package com.devsuperior.dscatalog.services.validation;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

//Classe usada para implantar toda lógica da validação (Igual uma regra de negócio)
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> { //interface do javax validation
										//UserInsertDTO é o tipo da classe que vai receber a anotation
	
	@Autowired UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override //método do tipo se é verdadeiro ou falso
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>(); //Lista vazia
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		//Cria toda validação para verificar se já existe o e-mail no banco e insere no errors validation
		User user = repository.findByEmail(dto.getEmail());
		if(user != null) {
			list.add(new FieldMessage("email", "Email já existe" ));
		}
		
		//Insere a lista dentro do validation (Insere os erros)
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty(); //Testa se a lista está vazia (Siginifica que nenhum teste deu erro)
	}
}