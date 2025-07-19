package com.sjfjuristas.plataforma.backend.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"Usuarios\"", schema = "schema_sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "usuarios_cpf_uq", columnNames = {"cpf"}),
        @UniqueConstraint(name = "usuarios_email_uq", columnNames = {"email"})
})
public class Usuario implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usuario_id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Size(max = 14)
    @Column(name = "cpf", length = 14)
    private String cpf;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "hash_senha")
    private String hashSenha;

    @Size(max = 20)
    @Column(name = "telefone_whatsapp", length = 20)
    private String telefoneWhatsapp;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "endereco_completo", length = Integer.MAX_VALUE)
    private String enderecoCompleto;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_cadastro")
    private OffsetDateTime dataCadastro;

    @Column(name = "ultimo_login")
    private OffsetDateTime ultimoLogin;

    @ColumnDefault("true")
    @Column(name = "ativo")
    private Boolean ativo = true;

    @Size(max = 255)
    @Column(name = "token_recuperacao_senha")
    private String tokenRecuperacaoSenha;

    @Column(name = "validade_token_recuperacao")
    private OffsetDateTime validadeTokenRecuperacao;

    @ColumnDefault("false")
    @Column(name = "email_verificado")
    private Boolean emailVerificado = false;

    @ColumnDefault("false")
    @Column(name = "cadastro_aprovado")
    private Boolean cadastroAprovado = false;

    @Size(max = 255)
    @Column(name = "token_verificacao_email")
    private String tokenVerificacaoEmail;

    @Column(name = "data_aceite_termos_app")
    private OffsetDateTime dataAceiteTermosApp;

    @ColumnDefault("false")
    @Column(name = "aceitou_termos_app")
    private Boolean aceitouTermosApp;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"perfil_id_PerfisUsuario\"")
    private PerfilUsuario perfilIdPerfisusuario;

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<ChavePixUsuario> chavesPixUsuarios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<ComprovantePagamento> comprovantesPagamentos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<DocumentoProposta> documentosPropostas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<Emprestimo> emprestimos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<LogAuditoria> logAuditorias = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<Notificacao> notificacoes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<PagamentoParcela> pagamentosParcelas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuarioIdUsuarios")
    private Set<PropostaEmprestimo> propostasEmprestimos = new LinkedHashSet<>();

    // UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: etornar os perfis/permissões do usuário.

        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email; // O "username" para o Spring Security será o e-mail
    }

    @Override
    public String getPassword() {
        return this.hashSenha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // TODO: lógica para expiração de conta
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // TODO: lógica para bloqueio de conta
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // TODO: lógica para expiração de credenciais
    }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }
}