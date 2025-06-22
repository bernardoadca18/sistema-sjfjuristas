package com.sjfjuristas.plataforma.backend.dto.ConfiguracaoSistema;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfiguracaoRequestDTO 
{
    @NotBlank(message = "A chave de configuração é obrigatória.")
    @Size(max = 100, message = "A chave não pode exceder 100 caracteres.")
    private String chaveConfig;

    @NotBlank(message = "O valor da configuração é obrigatório.")
    private String valorConfig;

    @NotBlank(message = "O tipo de dado é obrigatório.")
    @Size(max = 50, message = "O tipo de dado não pode exceder 50 caracteres.")
    private String tipoDado;

    private String descricao;

    @Size(max = 50, message = "O grupo não pode exceder 50 caracteres.")
    private String grupoConfig;
}