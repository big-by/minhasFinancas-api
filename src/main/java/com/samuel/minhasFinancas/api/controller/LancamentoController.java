package com.samuel.minhasFinancas.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.minhasFinancas.api.dto.AtualizaStatusDTO;
import com.samuel.minhasFinancas.api.dto.LancamentoDTO;
import com.samuel.minhasFinancas.exception.RegraNegocioException;
import com.samuel.minhasFinancas.model.entity.Lancamento;
import com.samuel.minhasFinancas.model.entity.Usuario;
import com.samuel.minhasFinancas.model.enums.StatusLancamento;
import com.samuel.minhasFinancas.model.enums.TipoLancamento;
import com.samuel.minhasFinancas.service.LancamentoService;
import com.samuel.minhasFinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {
	
	private final LancamentoService lancamentoService;
	
	private final UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto)
	{
		try {
			Lancamento entidade = converter(dto);
			entidade = lancamentoService.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable Long id,
			@RequestBody LancamentoDTO dto) 
	{
		return lancamentoService.obterPorId(id).map( entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () ->
			new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable Long id)
	{
		return lancamentoService.obterPorId(id)
				.map(entidade -> {
					lancamentoService.deletar(entidade);
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}).orElseGet(() -> 
					new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST)
				);
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus( @PathVariable("id") Long id , 
			@RequestBody AtualizaStatusDTO dto ) {
		return lancamentoService.obterPorId(id).map( entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
			}
			
			try {
				entity.setStatus(statusSelecionado);
				lancamentoService.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		
		}).orElseGet( () ->
			new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST)
			);
	}
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value="descricao", required=false) String descricao,
			@RequestParam(value="ano", required=false) Long ano,
			@RequestParam(value="mes", required=false) Long mes,
			@RequestParam(value="usuario", required=false) Long idUsuario)
	{
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(usuario.isPresent()) {
			lancamentoFiltro.setUsuario(usuario.get());
		} else {
			return ResponseEntity.badRequest().body("Não encontrou usuário");
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos); 
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService
			.obterPorId(dto.getUsuario())
			.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));
			
		
		lancamento.setUsuario(usuario);
		if(dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		return lancamento;
		
	}

}
