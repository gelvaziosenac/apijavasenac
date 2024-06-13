package bancocrudspringboot.controller;

import bancocrudspringboot.exception.ResourceNotFoundException;
import bancocrudspringboot.model.ConsultaPadrao;
import bancocrudspringboot.model.Usuario;
import bancocrudspringboot.model.Login;
import bancocrudspringboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;


@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class UsuarioController {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UsuarioRepository usuarioRepository;

	// Listar todos os usuarios
	@GetMapping("/usuarios")
	@ResponseStatus(HttpStatus.OK)
	public List<Usuario> getAllCadastros() {
		return this.usuarioRepository.findAll();
	}

	// pegar o usuario pelo id
	@GetMapping("/usuarios/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Usuario> getCadastroById(@PathVariable(value = "id") Long cadastroId)
			throws ResourceNotFoundException {

		Usuario cadastro = usuarioRepository.findById(cadastroId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para o ID :: " + cadastroId));
		return ResponseEntity.ok().body(cadastro);

	}

	// Salvar dados
	@PostMapping("/usuarios")
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario createCadastro(@RequestBody Usuario cadastro) {
		return this.usuarioRepository.save(cadastro);
	}

	@PutMapping("/usuarios/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Usuario> updateCadastro(@PathVariable(value = "id") Long cadastroId,
														@Validated @RequestBody Usuario cadastroCaracteristicas) throws ResourceNotFoundException {
		Usuario cadastro = usuarioRepository.findById(cadastroId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para o ID :: " + cadastroId));

		cadastro.setEmail(cadastroCaracteristicas.getEmail());
		cadastro.setNome(cadastroCaracteristicas.getNome());
		cadastro.setSenha(cadastroCaracteristicas.getSenha());

		return ResponseEntity.ok(this.usuarioRepository.save(cadastro));
	}

	@DeleteMapping("/usuarios/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Boolean> deleteCadastro(@PathVariable(value = "id") Long cadastroId)
			throws ResourceNotFoundException {
		Usuario cadastro = usuarioRepository.findById(cadastroId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para o ID :: " + cadastroId));

		this.usuarioRepository.delete(cadastro);

		Map<String, Boolean> resposta = new HashMap<>();

		resposta.put("cadastro deletado", Boolean.TRUE);

		return resposta;
	}

	@PostMapping("/consultausuarios")
	@ResponseStatus(HttpStatus.OK)
	public List<Usuario> consultaCadastro(@Validated @RequestBody ConsultaPadrao cadastro) throws ResourceNotFoundException {

		String campoUsuario = cadastro.getCampo();
		List<Usuario> listaUsuario = new ArrayList<>();

		switch (campoUsuario) {
            case "id":
                Usuario usuario = usuarioRepository.findById(Long.parseLong(cadastro.getValor1()))
                        .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado para o ID :: " + cadastro.getValor1()));

                listaUsuario.add(usuario);
				break;
            case "email":
				listaUsuario = this.usuarioRepository.findUsuarioByEmail(cadastro.getValor1());
				break;
            case "nome":
				listaUsuario = this.usuarioRepository.findUsuarioByNome(cadastro.getValor1());
				break;
			default:
				listaUsuario = this.usuarioRepository.findAll();
				break;
        }

		return listaUsuario;
	}

	// Login
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public Usuario login(@Validated @RequestBody Login cadastro) throws ResourceNotFoundException {

		String email = cadastro.getEmail();
		String senha = cadastro.getSenha();

		Usuario usuario = this.usuarioRepository.findUsuarioByEmailAndSenha(email, senha)
			.orElseThrow(() -> new ResourceNotFoundException("Usuario ou senha inválido!"));

		return usuario;
	}

}