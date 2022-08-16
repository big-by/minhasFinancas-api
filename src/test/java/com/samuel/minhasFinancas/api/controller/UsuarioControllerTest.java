package com.samuel.minhasFinancas.api.controller;

import com.samuel.minhasFinancas.exception.ErroAutenticacao;
import com.samuel.minhasFinancas.exception.RegraNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.minhasFinancas.api.dto.UsuarioDTO;
import com.samuel.minhasFinancas.model.entity.Usuario;
import com.samuel.minhasFinancas.service.LancamentoService;
import com.samuel.minhasFinancas.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

	public static final String EMAIL = "usuario@email.com";
	public static final String SENHA = "123";
	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	MockMvc mvc;

	@MockBean
	UsuarioService usuarioService;

	@MockBean
	LancamentoService lancamentoService;

	private UsuarioDTO povoadorUsuarioDTO(){
		return UsuarioDTO.builder()
			.email(EMAIL)
			.senha(SENHA)
			.build();
	}

	private Usuario povoadorUsuario() {
		return Usuario.builder()
				.id(1L)
				.email(EMAIL)
				.senha(SENHA)
				.build();
	}

	@Test
	public void testarAutenticarUsuario( ) throws Exception {
		UsuarioDTO dto = povoadorUsuarioDTO();
		Usuario usuario = povoadorUsuario();

		Mockito.when(usuarioService.autenticar(EMAIL, SENHA)).thenReturn(usuario);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(API.concat("/login"))
			.accept(JSON)
			.contentType(JSON)
			.content(json);

		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

	}

	@Test
	public void testarErroAutenticarUsuario( ) throws Exception {
		UsuarioDTO dto = povoadorUsuarioDTO();

		Mockito.when(usuarioService.autenticar(EMAIL, SENHA))
				.thenThrow(ErroAutenticacao.class);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API.concat("/login"))
				.accept(JSON)
				.contentType(JSON)
				.content(json);

		mvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	public void testarCriarUsuario( ) throws Exception {
		UsuarioDTO dto = povoadorUsuarioDTO();
		Usuario usuario = povoadorUsuario();

		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class)))
				.thenReturn(usuario);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(API)
			.accept(JSON)
			.contentType(JSON)
			.content(json);

		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

	}

	@Test
	public void testarErroCriarUsuario( ) throws Exception {
		UsuarioDTO dto = povoadorUsuarioDTO();

		Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class)))
				.thenThrow(RegraNegocioException.class);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(API)
			.accept(JSON)
			.contentType(JSON)
			.content(json);

		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}
	
	

}
