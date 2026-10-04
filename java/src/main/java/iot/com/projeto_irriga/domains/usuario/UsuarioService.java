package iot.com.projeto_irriga.domains.usuario;


import iot.com.projeto_irriga.dto.usuario.UsuarioLoginDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ResponseUtil responseUtil;

    public UsuarioService (UsuarioRepository usuarioRepository, ResponseUtil responseUtil) {
        this.usuarioRepository = usuarioRepository;
        this.responseUtil = responseUtil;
    }

    public ApiResponse sistemaLogin(UsuarioLoginDTO usuarioDatas ){

        UsuarioEntity usuario = this.procurarUsuarioPeloEmail(usuarioDatas.email());

        if(usuario == null){
            log.error("Usuario não existe");
            return this.responseUtil.error("User not Found","Crendenciais informadas incorretas", HttpStatus.CONFLICT);

        }

        if (!usuario.getSenha().equals(usuarioDatas.senha())){
            log.error("Senha Digitada incorreta");
            return this.responseUtil.error("User not Found","Crendenciais informadas incorretas", HttpStatus.CONFLICT);
        }


        return  this.responseUtil.sucess("Usuario Encontrado", "Usuario Encontrado", HttpStatus.OK);


    }

    private UsuarioEntity procurarUsuarioPeloEmail(String email){

        Optional<UsuarioEntity> usuario = this.usuarioRepository.findByEmail(email);

        return usuario.orElse(null);

    }




}
