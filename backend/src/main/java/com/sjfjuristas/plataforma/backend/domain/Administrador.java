package com.sjfjuristas.plataforma.backend.domain;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"Administradores\"", schema = "schema_sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "administradores_email_uq", columnNames = {"email"}),
        @UniqueConstraint(name = "Administradores_matricula_funcionario_uq", columnNames = {"matricula_funcionario"})
})
public class Administrador implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "adminstrador_id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Size(max = 255)
    @Column(name = "username")
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "hash_senha")
    private String hashSenha;

    @Size(max = 20)
    @Column(name = "telefone_contato", length = 20)
    private String telefoneContato;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_cadastro", nullable = false)
    private OffsetDateTime dataCadastro;

    @Column(name = "ultimo_login")
    private OffsetDateTime ultimoLogin;

    @ColumnDefault("true")
    @Column(name = "ativo")
    private Boolean ativo;

    @Size(max = 255)
    @Column(name = "token_recuperacao_senha")
    private String tokenRecuperacaoSenha;

    @Column(name = "validade_token_recuperacao")
    private OffsetDateTime validadeTokenRecuperacao;

    @ColumnDefault("false")
    @Column(name = "email_verificado")
    private Boolean emailVerificado;

    @Size(max = 255)
    @Column(name = "token_verificacao_email")
    private String tokenVerificacaoEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"perfil_id_PerfisUsuario\"")
    private PerfilUsuario perfilIdPerfisusuario;

    @Size(max = 100)
    @Column(name = "cargo_interno", length = 100)
    private String cargoInterno;

    @Size(max = 100)
    @Column(name = "departamento", length = 100)
    private String departamento;

    @Size(max = 50)
    @Column(name = "matricula_funcionario", length = 50)
    private String matriculaFuncionario;

    @ManyToMany
    @JoinTable(name = "many_Administradores_handle_many_ComprovantesPagamento",
            joinColumns = @JoinColumn(name = "adminstrador_id_Administradores"),
            inverseJoinColumns = @JoinColumn(name = "comprovante_id_ComprovantesPagamento"))
    private Set<ComprovantePagamento> comprovantesPagamentos = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "many_Administradores_handle_many_ConfiguracoesSistema",
            joinColumns = @JoinColumn(name = "adminstrador_id_Administradores"),
            inverseJoinColumns = @JoinColumn(name = "config_id_ConfiguracoesSistema"))
    private Set<ConfiguracaoSistema> configuracoesSistemas = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "many_Administradores_handle_many_PagamentosParcela",
            joinColumns = @JoinColumn(name = "adminstrador_id_Administradores"),
            inverseJoinColumns = @JoinColumn(name = "pagamento_id_PagamentosParcela"))
    private Set<PagamentoParcela> pagamentosParcelas = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "many_Administradores_handle_many_PropostasEmprestimo",
            joinColumns = @JoinColumn(name = "adminstrador_id_Administradores"),
            inverseJoinColumns = @JoinColumn(name = "proposta_id_PropostasEmprestimo"))
    private Set<PropostaEmprestimo> propostasEmprestimos = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        if (this.perfilIdPerfisusuario == null)
        {
            return Collections.emptyList();
        }
        
        String roleName = "ROLE_" + this.perfilIdPerfisusuario.getNomePerfil().toUpperCase();
        
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword()
    {
        return this.hashSenha;
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    public String getEmail()
    {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return this.ativo;
    }
}