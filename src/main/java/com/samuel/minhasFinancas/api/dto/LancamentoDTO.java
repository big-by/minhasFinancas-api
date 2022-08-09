package com.samuel.minhasFinancas.api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LancamentoDTO {

	private Long id;
	
	private String descricao;

	private Long mes;
	
	private Long ano;
	
	private Long usuario;
	
	private BigDecimal valor;
	
	private String tipo;
	
	private String status;

}
