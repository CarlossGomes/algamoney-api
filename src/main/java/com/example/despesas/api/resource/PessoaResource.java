package com.example.despesas.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.despesas.api.event.RecursoCriadoEvent;
import com.example.despesas.api.model.dtos.PessoaDTO;
import com.example.despesas.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<PessoaDTO> listar() {
		return pessoaService.listar();
	}

	@PostMapping
	public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody PessoaDTO pessoaDTO, HttpServletResponse response) {
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaDTO.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.salvar(pessoaDTO));
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
		return ResponseEntity.ok(pessoaService.buscarPorCodigo(codigo));
		// return pessoaDTO != null ? ResponseEntity.ok(pessoaDTO) :
		// ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		pessoaService.remover(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long codigo, @Valid @RequestBody PessoaDTO pessoaDTO) {
		return ResponseEntity.ok(pessoaService.atualizar(codigo, pessoaDTO));
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaService.atualizaPropriedadeAtivo(codigo, ativo);
	}

}
