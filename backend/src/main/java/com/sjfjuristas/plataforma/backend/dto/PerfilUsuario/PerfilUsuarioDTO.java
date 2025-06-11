package com.sjfjuristas.plataforma.backend.dto.PerfilUsuario;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class PerfilUsuarioDTO 
{
    private UUID id;
    private String nome;
    private String descricao;    
}
