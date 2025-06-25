package com.sjfjuristas.plataforma.backend.dto.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.Administrador;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO
{
    private UUID id;
    private String nomeCompleto;
    private String email;
    private String telefoneContatoInterno;
    private UUID perfilId;
    private String perfilName;
    private String cargoInterno;
    private String departamento;
    private String matriculaFuncionario;
    private String dataCriacao;
    private boolean ativo;

    public AdminResponseDTO(Administrador entity)
    {
        this.id = entity.getId();
        this.nomeCompleto = entity.getNomeCompleto();
        this.email = entity.getEmail();
        this.telefoneContatoInterno = entity.getTelefoneContato();
        this.perfilId = entity.getPerfilIdPerfisusuario();
        this.perfilName = "Adminstrador";
        this.cargoInterno = entity.getCargoInterno();
        this.departamento = entity.getDepartamento();
        this.matriculaFuncionario = entity.getMatriculaFuncionario();
        this.dataCriacao = entity.getDataCadastro().toString();
        this.ativo = entity.getAtivo();
    }
}
