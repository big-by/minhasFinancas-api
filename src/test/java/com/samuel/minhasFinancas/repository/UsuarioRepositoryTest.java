package com.samuel.minhasFinancas.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.samuel.minhasFinancas.model.entity.Usuario;
import com.samuel.minhasFinancas.model.repository.UsuarioRepository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	public UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarExistenciaEmail() {
		
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		entityManager.persist(usuario);
		
		boolean result = repository.existsByEmail("usuario@email.com");
		
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void testarExcecaoNaoHouverEmail() {
		boolean result = repository.existsByEmail("usuario@email.com");
		
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void testarPersistirUsuario() {
		Usuario usuario = povoarUsuario();
		
		Usuario usuarioSalvo = repository.save(usuario);
		
		Assertions.assertThat(usuarioSalvo.getIdLong()).isNotNull();
		
	}
	
	@Test
	public void testarBuscaPorEmail() {
		Usuario usuario = povoarUsuario();
		entityManager.persist(usuario);
		
		Optional<Usuario> resultado = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(resultado.isPresent()).isTrue();
		
		Optional<Usuario> resultadoFalso = repository.findByEmail("test@test.com");
		
		Assertions.assertThat(resultadoFalso.isPresent()).isFalse();
	}
	
	public static Usuario povoarUsuario() {
		return Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("123456")
				.build();
	}
	
	
	
	
}
