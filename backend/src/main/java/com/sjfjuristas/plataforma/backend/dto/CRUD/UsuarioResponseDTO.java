package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO
{
    private UUID id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private BigDecimal rendaMensal;
    private OcupacaoResponseDTO ocupacao;
    private boolean ativo;
    private LocalDateTime dataInclusao;
    private LocalDateTime dataAlteracao;

    public UsuarioResponseDTO(Usuario entity, BigDecimal rendaMensal, OcupacaoResponseDTO ocupacao)
    {
        this.id = entity.getId();
        this.nome = entity.getNomeCompleto();
        this.email = entity.getEmail();
        this.telefone = entity.getTelefoneWhatsapp();
        this.cpf = entity.getCpf();
        this.dataNascimento = entity.getDataNascimento();
        this.rendaMensal = rendaMensal;
        this.ocupacao = ocupacao;
        this.ativo = entity.getAtivo();
        this.dataInclusao = entity.getDataCadastro().toLocalDateTime();
        this.dataAlteracao = LocalDateTime.now();
    }

    public UsuarioResponseDTO(Usuario entity)
    {
        this.id = entity.getId();
        this.nome = entity.getNomeCompleto();
        this.email = entity.getEmail();
        this.telefone = entity.getTelefoneWhatsapp();
        this.cpf = entity.getCpf();
        this.dataNascimento = entity.getDataNascimento();
        this.rendaMensal = entity.getRendaMensal();
        this.ocupacao = null;
        this.ativo = entity.getAtivo();
        this.dataInclusao = entity.getDataCadastro().toLocalDateTime();
        this.dataAlteracao = LocalDateTime.now();
    }
}
