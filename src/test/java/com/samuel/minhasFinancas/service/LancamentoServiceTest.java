package com.samuel.minhasFinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.samuel.minhasFinancas.exception.RegraNegocioException;
import com.samuel.minhasFinancas.model.entity.Lancamento;
import com.samuel.minhasFinancas.model.enums.StatusLancamento;
import com.samuel.minhasFinancas.model.enums.TipoLancamento;
import com.samuel.minhasFinancas.model.repository.LancamentoRepository;
import com.samuel.minhasFinancas.repository.LancamentoRepositoryTest;
import com.samuel.minhasFinancas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void salvarLancamento( ) {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow( RegraNegocioException.class ).when(service).validar(lancamentoASalvar);
		
		catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioException.class );
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		service.atualizar(lancamentoSalvo);
		
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
		
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		catchThrowableOfType( () -> service.atualizar(lancamento), NullPointerException.class );
		Mockito.verify(repository, Mockito.never()).save(lancamento);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		service.deletar(lancamento);
		
		Mockito.verify( repository ).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class );
		
		Mockito.verify( repository, Mockito.never() ).delete(lancamento);
	}
	
	
	@Test
	public void deveFiltrarLancamentos() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class)) ).thenReturn(lista);
		
		List<Lancamento> resultado = service.buscar(lancamento);
		
		assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
		
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		service.atualizarStatus(lancamento, novoStatus);
		
		assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
		
	}
	
	@Test
	public void deveObterUmLancamentoPorID() {
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		Optional<Lancamento> resultado =  service.obterPorId(id);
		
		assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveREtornarVazioQuandoOLancamentoNaoExiste() {
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when( repository.findById(id) ).thenReturn( Optional.empty() );
		
		Optional<Lancamento> resultado =  service.obterPorId(id);
		
		assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveObterSaldoPorUsuario() {
		Long idUsuario = 1l;
		
		Mockito.when( repository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(idUsuario, TipoLancamento.RECEITA, StatusLancamento.EFETIVADO)) 
				.thenReturn(BigDecimal.valueOf(100));
		
		Mockito.when( repository
				.obterSaldoPorTipoLancamentoEUsuarioEStatus(idUsuario, TipoLancamento.DESPESA, StatusLancamento.EFETIVADO)) 
				.thenReturn(BigDecimal.valueOf(50));
		
		BigDecimal saldo = service.obterSaldoPorUsuario(idUsuario);
		
		assertThat(saldo).isEqualTo(BigDecimal.valueOf(50));
	}

}
