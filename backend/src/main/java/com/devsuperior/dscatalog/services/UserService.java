package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service							//Classe do spring security
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class); //Para pegar os logs da aplicação
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncorder; //Para gerar o código
	
	
	
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {

		Page<User> list = repository.findAll(pageable);

		return list.map(x -> new UserDTO(x));

	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Nenhum id encontrado"));

		return new UserDTO(entity); // 
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {

		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncorder.encode(dto.getPassword())); //Gera o código a partir da senha do usuário
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) { //Trocado para o UserUpdateDTO para usar a anotation UserUpdateValid
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);

			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id não encontrado" + id);
		}

	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado" + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade");
		}
	}
	
	private void copyDtoToEntity(UserDTO dto, User entity) {   //Método auxiliar para adicionar os dados do produto ou fazer update
		
		
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		
		entity.getRoles().clear();
		for(RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role); 
		}
		
	}

	//Método do UserDetails para encontrar o e-mail 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repository.findByEmail(username);
		//Caso não encontre o e-mail do usuário, estoura uma exceção
		if(user == null) {
			logger.error("Usuário não encontrado: " + username);//Para lançar o log no console da aplicação
			throw new UsernameNotFoundException("E-mail não encontrado");
		}
		logger.info("User found: " + username); //Para lançar o log no console da aplicação
		return user;
	}

}
