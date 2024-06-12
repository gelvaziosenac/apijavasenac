package bancocrudspringboot.controller;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import bancocrudspringboot.exception.ResourceNotFoundException;
import bancocrudspringboot.model.Cliente;
import bancocrudspringboot.repository.ClienteRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/cliente")
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> getAll() {
        return this.clienteRepository.findAll();
    }

    // Inserir
    @PostMapping("/cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente createCadastro(@RequestBody Cliente cadastro) {
        return this.clienteRepository.save(cadastro);
    }

    // alterar
    @PutMapping("/cliente/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cliente> updateCadastro(@PathVariable(value = "id")
                                                  Long cadastroId,
                                                  @Validated
                                                  @RequestBody Cliente cadastroCaracteristicas) throws ResourceNotFoundException {
        Cliente cadastro = clienteRepository.findById(cadastroId)
            .orElseThrow(() -> new ResourceNotFoundException("Cadastro não encontrado para o ID: " + cadastroId));

        cadastro.setNome(cadastroCaracteristicas.getNome());
        cadastro.setCpf(cadastroCaracteristicas.getCpf());
        cadastro.setEndereco(cadastroCaracteristicas.getEndereco());
        
        cadastro.setNumero(cadastroCaracteristicas.getNumero());
        cadastro.setComplemento(cadastroCaracteristicas.getComplemento());
        cadastro.setBairro(cadastroCaracteristicas.getBairro());
        
        cadastro.setCidade(cadastroCaracteristicas.getCidade());
        cadastro.setEstado(cadastroCaracteristicas.getEstado());

        return ResponseEntity.ok(this.clienteRepository.save(cadastro));
    }

    @DeleteMapping("/cliente/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> deleteCadastro(@PathVariable(value = "id") Long cadastroId) throws ResourceNotFoundException {
        Cliente cadastro = clienteRepository.findById(cadastroId)
            .orElseThrow(() -> new ResourceNotFoundException("Cadastro não encontrado para o ID: " + cadastroId));

        this.clienteRepository.delete(cadastro);

        Map<String, Boolean> resposta = new HashMap<>();

        resposta.put("cadastro deletado", Boolean.TRUE);

        return resposta;
    }

}