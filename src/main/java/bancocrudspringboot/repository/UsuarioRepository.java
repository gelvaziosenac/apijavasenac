package bancocrudspringboot.repository;

import bancocrudspringboot.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    @Query(value="select u.* from usuario u where email like concat(:email, '%')", nativeQuery = true)
    List<Usuario>findUsuarioByEmail(@Param("email")String email);

    @Query(value="select u.* from usuario u where nome like concat(:nome, '%')", nativeQuery = true)
    List<Usuario>findUsuarioByNome(@Param("nome")String nome);

    Optional<Usuario> findUsuarioByEmailAndSenha(String email, String senha);

}
