package com.samuel.minhasFinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuel.minhasFinancas.exception.ErroAutenticacao;
import com.samuel.minhasFinancas.exception.RegraNegocioException;
import com.samuel.minhasFinancas.model.entity.Usuario;
import com.samuel.minhasFinancas.model.repository.UsuarioRepository;
import com.samuel.minhasFinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Usuário ou senha incorreto");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email){
		boolean existeEmail = repository.existsByEmail(email);
		
		if(existeEmail) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}
