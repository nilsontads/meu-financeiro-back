package br.meufinanceiroback.controller;

import br.meufinanceiroback.dto.UsuarioDTO;
import br.meufinanceiroback.model.Usuario;
import br.meufinanceiroback.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody UsuarioDTO dto) {
        System.out.println(">>> Salvando novo usuário: " + dto.getEmail());

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTelefone(dto.getTelefone());

        Usuario salvo = usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioDTO(
                        salvo.getNome(),
                        salvo.getEmail(),
                        salvo.getSenha(),
                        salvo.getTelefone()
                ));
    }

    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(usuario -> new UsuarioDTO(usuario.getNome(), usuario.getEmail(), usuario.getTelefone(),usuario.getSenha()
        )).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(usuario -> ResponseEntity.ok(new UsuarioDTO(usuario.getNome(), usuario.getEmail(), usuario.getTelefone(),usuario.getSenha()
        ))).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(dto.getNome());
                    usuario.setEmail(dto.getEmail());
                    usuario.setTelefone(dto.getTelefone());
                    usuarioRepository.save(usuario);

                    return ResponseEntity.ok(
                            new UsuarioDTO(
                                    usuario.getNome(),
                                    usuario.getEmail(),
                                    usuario.getSenha(),
                                    usuario.getTelefone()
                            )
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

}