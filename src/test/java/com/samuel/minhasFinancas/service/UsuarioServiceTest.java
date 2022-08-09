package com.samuel.minhasFinancas.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.samuel.minhasFinancas.exception.RegraNegocioException;
import com.samuel.minhasFinancas.model.entity.Usuario;
import com.samuel.minhasFinancas.model.repository.UsuarioRepository;
import com.samuel.minhasFinancas.service.impl.UsuarioServiceImpl;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	@MockBean
	UsuarioRepository repository;
	
	@Test
	public void testarValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		service.validarEmail("email@email.com");
	}
	
	@Test
	public void testarErroValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		Throwable exception = Assertions.catchThrowable(() -> service.validarEmail("email@email.com"));
		
		Assertions.assertThat(exception)
			.isInstanceOf(RegraNegocioException.class)
			.hasMessage("Já existe um usuário cadastrado com este e-mail");
	}
	
	@Test
	public void testarAutenticarUsuarioComSucesso() {
		String email = "email@email.com";
		String senha = "senha";
	
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario result = service.autenticar(email, senha);
		
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void testarSalvarUsuarioComSucesso() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		
		String email = "email@email.com";
		String senha = "senha";
		String nome = "Nome";
		
		Usuario usuario = Usuario.builder().nome(nome).email(email).senha(senha).id(1L).build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo(nome);
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo(email);
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo(senha);
		
	}
	

}
