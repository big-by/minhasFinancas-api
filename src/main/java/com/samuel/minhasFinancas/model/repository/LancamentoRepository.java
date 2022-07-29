package com.samuel.minhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuel.minhasFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
