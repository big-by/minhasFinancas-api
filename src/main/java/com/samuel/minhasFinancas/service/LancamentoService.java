package com.samuel.minhasFinancas.service;

import java.util.List;

import com.samuel.minhasFinancas.model.entity.Lancamento;
import com.samuel.minhasFinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento filtro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
}
