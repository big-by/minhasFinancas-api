package com.samuel.minhasFinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.samuel.minhasFinancas.exception.RegraNegocioException;
import com.samuel.minhasFinancas.model.entity.Lancamento;
import com.samuel.minhasFinancas.model.entity.Usuario;
import com.samuel.minhasFinancas.model.enums.StatusLancamento;
import com.samuel.minhasFinancas.model.enums.TipoLancamento;
import com.samuel.minhasFinancas.model.repository.LancamentoRepository;
import com.samuel.minhasFinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{
	
	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional
	public List<Lancamento> buscar(Lancamento filtro) {
		Example example = Example.of(filtro,
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {
		if(lancamento.getDescricao() == null ||
				lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descrição válida.");
		}
		
		if(lancamento.getMes() == null ||
				lancamento.getMes() < 1 || lancamento.getMes() > 12 ) {
			throw new RegraNegocioException("Informe um mês válido");
		}
		
		if(lancamento.getAno() == null ||
				lancamento.getAno().toString().length() < 4) {
			throw new RegraNegocioException("Informe um ano válido.");
		}
		
		if(lancamento.getUsuario() == null ||
				lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um usuário válido.");
		}
		
		if(lancamento.getValor() == null ||
				lancamento.getValor().compareTo(BigDecimal.ZERO) < 1){
			throw new RegraNegocioException("Informe um valor válido.");
		}
		
		if(lancamento.getTipo() == null){
			throw new RegraNegocioException("Informe um tipo de lançamento.");
		}
	}
	
	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public BigDecimal obterSaldoPorUsuario(Long id) {
		
		BigDecimal receitas = repository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO);
		BigDecimal despesas = repository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(id, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);
		
		if(receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		
		if(despesas == null) {
			despesas = BigDecimal.ZERO;
		}
		
		return receitas.subtract(despesas);
	}


}
