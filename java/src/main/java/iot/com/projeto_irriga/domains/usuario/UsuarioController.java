package iot.com.projeto_irriga.domains.usuario;

import iot.com.projeto_irriga.dto.usuario.UsuarioLoginDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {

        this.usuarioService = usuarioService;

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UsuarioLoginDTO usuarioDatas) {
        ApiResponse loginResponse = this.usuarioService.sistemaLogin(usuarioDatas);

        return ResponseEntity.status(loginResponse.getStatus()).body(loginResponse);

    }
}
