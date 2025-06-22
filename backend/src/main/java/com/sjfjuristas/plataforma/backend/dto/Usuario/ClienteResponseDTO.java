package com.sjfjuristas.plataforma.backend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.UUID;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO
{
    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String telefoneWhatsapp;
    private String dataNascimento; // Formato ISO 8601 (YYYY-MM-DD)
    private String dataCadastro; // Formato ISO 8601 (YYYY-MM-DDTHH:mm:ssZ)
    private boolean emailVerificado;


    public ClienteResponseDTO(Usuario usuario)
    {
        this.id = usuario.getId();
        this.nomeCompleto = usuario.getNomeCompleto();
        this.cpf = usuario.getCpf();
        this.email = usuario.getEmail();
        this.telefoneWhatsapp = usuario.getTelefoneWhatsapp();
        this.dataNascimento = usuario.getDataNascimento() != null ? usuario.getDataNascimento().toString() : null;
        this.dataCadastro = usuario.getDataCadastro() != null ? usuario.getDataCadastro().toString() : null;
        this.emailVerificado = usuario.getEmailVerificado();
    }
}