-- =================================================================
-- CRIAÇÃO DO SCHEMA E DAS TABELAS (ESTRUTURA)
-- =================================================================
CREATE SCHEMA IF NOT EXISTS schema_sjfjuristas;

CREATE TABLE schema_sjfjuristas.perfis_usuario (
    perfil_id uuid NOT NULL,
    nome_perfil varchar(50) NOT NULL,
    PRIMARY KEY (perfil_id),
    CONSTRAINT perfisusuario_nome_perfil_uq UNIQUE (nome_perfil)
);

CREATE TABLE schema_sjfjuristas.usuarios (
    usuario_id uuid NOT NULL,
    nome_completo varchar(255),
    cpf varchar(14),
    email varchar(255) NOT NULL,
    hash_senha varchar(255),
    telefone_whatsapp varchar(20),
    data_nascimento date,
    endereco_completo text,
    data_cadastro timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    ultimo_login timestamp(6) with time zone,
    ativo boolean DEFAULT true,
    token_recuperacao_senha varchar(255),
    validade_token_recuperacao timestamp(6) with time zone,
    email_verificado boolean DEFAULT false,
    token_verificacao_email varchar(255),
    data_aceite_termos_app timestamp(6) with time zone,
    aceitou_termos_app boolean DEFAULT false,
    cadastro_aprovado boolean DEFAULT false,
    nome_completo_mae varchar(255),
    renda_mensal numeric(16,2),
    perfil_id_perfis_usuario uuid,
    PRIMARY KEY (usuario_id),
    CONSTRAINT usuarios_cpf_uq UNIQUE (cpf),
    CONSTRAINT usuarios_email_uq UNIQUE (email),
    CONSTRAINT fk_usuarios_perfis_usuario FOREIGN KEY (perfil_id_perfis_usuario) REFERENCES schema_sjfjuristas.perfis_usuario(perfil_id) ON DELETE RESTRICT
);

CREATE TABLE schema_sjfjuristas.administradores (
    adminstrador_id uuid NOT NULL,
    nome_completo varchar(255),
    email varchar(255) NOT NULL,
    hash_senha varchar(255),
    telefone_contato varchar(20),
    data_cadastro timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ultimo_login timestamp(6) with time zone,
    ativo boolean DEFAULT true,
    token_recuperacao_senha varchar(255),
    validade_token_recuperacao timestamp(6) with time zone,
    email_verificado boolean DEFAULT false,
    token_verificacao_email varchar(255),
    perfil_id_perfis_usuario uuid,
    cargo_interno varchar(100),
    departamento varchar(100),
    matricula_funcionario varchar(50),
    PRIMARY KEY (adminstrador_id),
    CONSTRAINT administradores_email_uq UNIQUE (email),
    CONSTRAINT administradores_matricula_funcionario_uq UNIQUE (matricula_funcionario)
);

CREATE TABLE schema_sjfjuristas.tipos_chave_pix (
    tipo_chave_pix_id uuid NOT NULL,
    nome_tipo varchar(50) NOT NULL,
    PRIMARY KEY (tipo_chave_pix_id),
    CONSTRAINT pix_nome_tipo_uq UNIQUE (nome_tipo)
);

CREATE TABLE schema_sjfjuristas.chaves_pix_usuario (
    chave_pix_id uuid NOT NULL,
    valor_chave varchar(255) NOT NULL,
    data_cadastro timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    ativa_para_desembolso boolean DEFAULT false,
    verificada boolean DEFAULT false,
    data_verificacao timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    tipo_chave_pix_id_tipos_chave_pix uuid,
    usuario_id_usuarios uuid,
    PRIMARY KEY (chave_pix_id),
    CONSTRAINT fk_chavespix_tiposchavepix FOREIGN KEY (tipo_chave_pix_id_tipos_chave_pix) REFERENCES schema_sjfjuristas.tipos_chave_pix(tipo_chave_pix_id) ON DELETE RESTRICT,
    CONSTRAINT fk_chavespix_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE CASCADE
);

CREATE TABLE schema_sjfjuristas.status_proposta (
    status_proposta_id uuid NOT NULL,
    nome_status varchar(50) NOT NULL,
    PRIMARY KEY (status_proposta_id),
    CONSTRAINT statusproposta_nome_status_uq UNIQUE (nome_status)
);

CREATE TABLE schema_sjfjuristas.propostas_emprestimo (
    proposta_id uuid NOT NULL,
    valor_solicitado numeric(16,2) NOT NULL,
    nome_completo_solicitante varchar(255) NOT NULL,
    cpf_solicitante varchar(14) NOT NULL,
    email_solicitante varchar(255) NOT NULL,
    telefone_whatsapp_solicitante varchar(20),
    data_solicitacao timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    ip_solicitacao varchar(45),
    user_agent_solicitacao text,
    termos_aceitos_lp boolean DEFAULT false NOT NULL,
    data_aceite_termos_lp timestamp(6) with time zone,
    motivo_negacao text,
    data_analise timestamp(6) with time zone,
    link_app_enviado boolean DEFAULT false,
    data_envio_link_app timestamp(6) with time zone,
    origem_captacao varchar(100) DEFAULT 'LandingPage',
    status_proposta_id_status_proposta uuid,
    usuario_id_usuarios uuid,
    PRIMARY KEY (proposta_id),
    CONSTRAINT fk_propostas_statusproposta FOREIGN KEY (status_proposta_id_status_proposta) REFERENCES schema_sjfjuristas.status_proposta(status_proposta_id) ON DELETE RESTRICT,
    CONSTRAINT fk_propostas_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE SET NULL
);

CREATE TABLE schema_sjfjuristas.tipos_documento (
    tipo_documento_id uuid NOT NULL,
    nome_documento varchar(100) NOT NULL,
    obrigatorio_proposta boolean DEFAULT true,
    descricao text,
    PRIMARY KEY (tipo_documento_id),
    CONSTRAINT tiposdocumento_nome_documento_uq UNIQUE (nome_documento)
);

CREATE TABLE schema_sjfjuristas.documentos_proposta (
    documento_id uuid NOT NULL,
    url_documento text NOT NULL,
    nome_arquivo_original varchar(255),
    tipo_mime varchar(100),
    tamanho_bytes bigint,
    data_upload timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    hash_documento varchar(255),
    status_validacao varchar(50) DEFAULT 'Pendente',
    data_validacao timestamp(6) with time zone,
    observacoes_validacao text,
    tipo_documento_id_tipos_documento uuid,
    usuario_id_usuarios uuid,
    proposta_id_propostas_emprestimo uuid,
    PRIMARY KEY (documento_id),
    CONSTRAINT fk_docsproposta_tiposdocumento FOREIGN KEY (tipo_documento_id_tipos_documento) REFERENCES schema_sjfjuristas.tipos_documento(tipo_documento_id) ON DELETE RESTRICT,
    CONSTRAINT fk_docsproposta_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE SET NULL,
    CONSTRAINT fk_docsproposta_propostasemprestimo FOREIGN KEY (proposta_id_propostas_emprestimo) REFERENCES schema_sjfjuristas.propostas_emprestimo(proposta_id) ON DELETE CASCADE
);

CREATE TABLE schema_sjfjuristas.status_emprestimo (
    status_emprestimo_id uuid NOT NULL,
    nome_status varchar(50) NOT NULL,
    PRIMARY KEY (status_emprestimo_id),
    CONSTRAINT statusemprestimo_nome_status_uq UNIQUE (nome_status)
);

CREATE TABLE schema_sjfjuristas.emprestimos (
    emprestimo_id uuid NOT NULL,
    valor_contratado numeric(16,2),
    valor_liberado numeric(16,2),
    taxa_juros_mensal_efetiva numeric(16,2),
    taxa_juros_diaria_efetiva numeric(16,2),
    cet_anual numeric(16,6),
    numero_total_parcelas integer,
    valor_parcela_diaria numeric(16,2),
    data_contratacao timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    data_primeiro_vencimento date,
    data_ultimo_vencimento date,
    saldo_devedor_atual numeric(16,2),
    data_solicitacao_desembolso timestamp(6) with time zone,
    data_desembolso_efetivo timestamp(6) with time zone,
    id_transacao_desembolso_psp varchar(255),
    iof_valor numeric(16,2) DEFAULT 0.00,
    seguro_valor numeric(16,2) DEFAULT 0.00,
    outras_taxas numeric(16,2) DEFAULT 0.00,
    data_inicio_cobranca_parcelas date,
    status_emprestimo_id_status_emprestimo uuid,
    usuario_id_usuarios uuid,
    proposta_id_propostas_emprestimo uuid,
    chave_pix_id_chaves_pix_usuario uuid,
    PRIMARY KEY (emprestimo_id),
    CONSTRAINT emprestimo_id_transacao_desembolso_psp_uq UNIQUE (id_transacao_desembolso_psp),
    CONSTRAINT emprestimos_proposta_id_uq UNIQUE (proposta_id_propostas_emprestimo),
    CONSTRAINT fk_emprestimos_statusemprestimo FOREIGN KEY (status_emprestimo_id_status_emprestimo) REFERENCES schema_sjfjuristas.status_emprestimo(status_emprestimo_id) ON DELETE RESTRICT,
    CONSTRAINT fk_emprestimos_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE RESTRICT,
    CONSTRAINT fk_emprestimos_propostas FOREIGN KEY (proposta_id_propostas_emprestimo) REFERENCES schema_sjfjuristas.propostas_emprestimo(proposta_id) ON DELETE RESTRICT,
    CONSTRAINT fk_emprestimos_chavespix FOREIGN KEY (chave_pix_id_chaves_pix_usuario) REFERENCES schema_sjfjuristas.chaves_pix_usuario(chave_pix_id) ON DELETE RESTRICT
);

CREATE TABLE schema_sjfjuristas.status_pagamento_parcela (
    status_pagamento_parcela_id uuid NOT NULL,
    nome_status varchar(50) NOT NULL,
    PRIMARY KEY (status_pagamento_parcela_id),
    CONSTRAINT statuspagamentoparcela_nome_status_uq UNIQUE (nome_status)
);

CREATE TABLE schema_sjfjuristas.parcelas_emprestimo (
    parcela_id uuid NOT NULL,
    numero_parcela integer NOT NULL,
    data_vencimento date NOT NULL,
    valor_principal_amortizado numeric(16,2) NOT NULL,
    valor_juros numeric(16,2) NOT NULL,
    valor_total_parcela numeric(16,2) NOT NULL,
    pix_copia_cola text,
    pix_qr_code_base64 text,
    data_geracao_pix timestamp(6) with time zone,
    id_transacao_geracao_pix_psp varchar(255),
    status_pagamento_parcela_id_status_pagamento_parcela uuid,
    emprestimo_id_emprestimos uuid,
    PRIMARY KEY (parcela_id),
    CONSTRAINT fk_parcelas_statuspagamento FOREIGN KEY (status_pagamento_parcela_id_status_pagamento_parcela) REFERENCES schema_sjfjuristas.status_pagamento_parcela(status_pagamento_parcela_id) ON DELETE RESTRICT,
    CONSTRAINT fk_parcelas_emprestimos FOREIGN KEY (emprestimo_id_emprestimos) REFERENCES schema_sjfjuristas.emprestimos(emprestimo_id) ON DELETE CASCADE
);

CREATE TABLE schema_sjfjuristas.pagamentos_parcela (
    pagamento_id uuid NOT NULL,
    valor_pago numeric(16,2) NOT NULL,
    data_pagamento_efetivo timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    meio_pagamento varchar(50) DEFAULT 'PIX',
    id_transacao_pagamento_psp varchar(255),
    data_confirmacao_manual timestamp(6) with time zone,
    webhook_payload_psp jsonb,
    observacoes text,
    usuario_id_usuarios uuid,
    emprestimo_id_emprestimos uuid,
    parcela_id_parcelas_emprestimo uuid,
    PRIMARY KEY (pagamento_id),
    CONSTRAINT pagamentoparcela_id_transacao_pagamento_psp_uq UNIQUE (id_transacao_pagamento_psp),
    CONSTRAINT fk_pagamentos_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE RESTRICT,
    CONSTRAINT fk_pagamentos_emprestimos FOREIGN KEY (emprestimo_id_emprestimos) REFERENCES schema_sjfjuristas.emprestimos(emprestimo_id) ON DELETE CASCADE,
    CONSTRAINT fk_pagamentos_parcelas FOREIGN KEY (parcela_id_parcelas_emprestimo) REFERENCES schema_sjfjuristas.parcelas_emprestimo(parcela_id) ON DELETE CASCADE
);

CREATE TABLE schema_sjfjuristas.comprovantes_pagamento (
    comprovante_id uuid NOT NULL,
    url_comprovante text NOT NULL,
    nome_arquivo_original varchar(255),
    tipo_mime varchar(100),
    tamanho_bytes bigint,
    data_upload timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    status_verificacao varchar(50) DEFAULT 'Pendente',
    data_verificacao_admin timestamp(6) with time zone,
    observacoes_verificacao text,
    usuario_id_usuarios uuid,
    parcela_id_parcelas_emprestimo uuid,
    pagamento_id_pagamentos_parcela uuid,
    PRIMARY KEY (comprovante_id),
    CONSTRAINT fk_comprovantes_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE SET NULL,
    CONSTRAINT fk_comprovantes_parcelas FOREIGN KEY (parcela_id_parcelas_emprestimo) REFERENCES schema_sjfjuristas.parcelas_emprestimo(parcela_id) ON DELETE CASCADE,
    CONSTRAINT fk_comprovantes_pagamentos FOREIGN KEY (pagamento_id_pagamentos_parcela) REFERENCES schema_sjfjuristas.pagamentos_parcela(pagamento_id) ON DELETE SET NULL
);

CREATE TABLE schema_sjfjuristas.notificacoes (
    notificacao_id uuid NOT NULL,
    titulo varchar(255) NOT NULL,
    mensagem text NOT NULL,
    tipo_notificacao varchar(50) NOT NULL,
    metadados jsonb,
    data_criacao timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    data_leitura timestamp(6) with time zone,
    lida boolean DEFAULT false,
    link_destino_app text,
    enviada_por_push boolean DEFAULT false,
    data_envio_email timestamp(6) with time zone,
    data_envio_push timestamp(6) with time zone,
    usuario_id_usuarios uuid,
    PRIMARY KEY (notificacao_id),
    CONSTRAINT fk_notificacoes_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE CASCADE
);

CREATE TABLE schema_sjfjuristas.log_auditoria (
    log_id uuid NOT NULL,
    timestamp_evento timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    perfil_usuario_no_evento varchar(50),
    acao_codigo varchar(100) NOT NULL,
    descricao_acao text,
    entidade_afetada varchar(100),
    id_entidade_afetada varchar(255),
    detalhes_antes jsonb,
    detalhes_depois jsonb,
    ip_origem varchar(45),
    user_agent text,
    sucesso boolean DEFAULT true,
    mensagem_erro text,
    usuario_id_usuarios uuid,
    PRIMARY KEY (log_id),
    CONSTRAINT fk_log_usuarios FOREIGN KEY (usuario_id_usuarios) REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE SET NULL
);

CREATE TABLE schema_sjfjuristas.configuracoes_sistema (
    config_id uuid NOT NULL,
    chave_config varchar(100) NOT NULL,
    valor_config text,
    tipo_dado varchar(50) DEFAULT 'TEXT',
    descricao text,
    grupo_config varchar(50),
    data_modificacao timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (config_id),
    CONSTRAINT configuracoessistema_chave_config_uq UNIQUE (chave_config)
);

CREATE TABLE schema_sjfjuristas.many_administradores_handle_many_comprovantes_pagamento (
    adminstrador_id_administradores uuid NOT NULL,
    comprovante_id_comprovantes_pagamento uuid NOT NULL,
    PRIMARY KEY (adminstrador_id_administradores, comprovante_id_comprovantes_pagamento),
    CONSTRAINT fk_many_admin_comp_admin FOREIGN KEY (adminstrador_id_administradores) REFERENCES schema_sjfjuristas.administradores(adminstrador_id),
    CONSTRAINT fk_many_admin_comp_comp FOREIGN KEY (comprovante_id_comprovantes_pagamento) REFERENCES schema_sjfjuristas.comprovantes_pagamento(comprovante_id)
);

CREATE TABLE schema_sjfjuristas.many_administradores_handle_many_configuracoes_sistema (
    adminstrador_id_administradores uuid NOT NULL,
    config_id_configuracoes_sistema uuid NOT NULL,
    PRIMARY KEY (adminstrador_id_administradores, config_id_configuracoes_sistema),
    CONSTRAINT fk_many_admin_conf_admin FOREIGN KEY (adminstrador_id_administradores) REFERENCES schema_sjfjuristas.administradores(adminstrador_id),
    CONSTRAINT fk_many_admin_conf_conf FOREIGN KEY (config_id_configuracoes_sistema) REFERENCES schema_sjfjuristas.configuracoes_sistema(config_id)
);

CREATE TABLE schema_sjfjuristas.many_administradores_handle_many_pagamentos_parcela (
    adminstrador_id_administradores uuid NOT NULL,
    pagamento_id_pagamentos_parcela uuid NOT NULL,
    PRIMARY KEY (adminstrador_id_administradores, pagamento_id_pagamentos_parcela),
    CONSTRAINT fk_many_admin_pag_admin FOREIGN KEY (adminstrador_id_administradores) REFERENCES schema_sjfjuristas.administradores(adminstrador_id),
    CONSTRAINT fk_many_admin_pag_pag FOREIGN KEY (pagamento_id_pagamentos_parcela) REFERENCES schema_sjfjuristas.pagamentos_parcela(pagamento_id)
);

CREATE TABLE schema_sjfjuristas.many_administradores_handle_many_propostas_emprestimo (
    adminstrador_id_administradores uuid NOT NULL,
    proposta_id_propostas_emprestimo uuid NOT NULL,
    PRIMARY KEY (adminstrador_id_administradores, proposta_id_propostas_emprestimo),
    CONSTRAINT fk_many_admin_prop_admin FOREIGN KEY (adminstrador_id_administradores) REFERENCES schema_sjfjuristas.administradores(adminstrador_id),
    CONSTRAINT fk_many_admin_prop_prop FOREIGN KEY (proposta_id_propostas_emprestimo) REFERENCES schema_sjfjuristas.propostas_emprestimo(proposta_id)
);

CREATE TABLE IF NOT EXISTS schema_sjfjuristas.ocupacoes (
    ocupacao_id UUID PRIMARY KEY,
    nome_ocupacao VARCHAR(255) NOT NULL UNIQUE,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE schema_sjfjuristas.usuario_ocupacoes (
    usuario_id_usuarios uuid NOT NULL,
    ocupacao_id_ocupacoes uuid NOT NULL,
    PRIMARY KEY (usuario_id_usuarios, ocupacao_id_ocupacoes),
    
    CONSTRAINT fk_usuarioocupacoes_usuarios FOREIGN KEY (usuario_id_usuarios) 
        REFERENCES schema_sjfjuristas.usuarios(usuario_id) ON DELETE CASCADE,
        
    CONSTRAINT fk_usuarioocupacoes_ocupacoes FOREIGN KEY (ocupacao_id_ocupacoes) 
        REFERENCES schema_sjfjuristas.ocupacoes(ocupacao_id) ON DELETE CASCADE
);

-- =================================================================
-- ALTERAÇÕES DE TABELAS (EVOLUÇÃO DO SCHEMA)
-- =================================================================
ALTER TABLE schema_sjfjuristas.parcelas_emprestimo ADD COLUMN IF NOT EXISTS pix_data_expiracao timestamp(6) with time zone;

ALTER TABLE schema_sjfjuristas.propostas_emprestimo
    ADD COLUMN IF NOT EXISTS valor_ofertado numeric(16,2),
    ADD COLUMN IF NOT EXISTS taxa_juros_ofertada numeric(16,4),
    ADD COLUMN IF NOT EXISTS num_parcelas_ofertado integer,
    ADD COLUMN IF NOT EXISTS data_deposito_prevista date,
    ADD COLUMN IF NOT EXISTS data_inicio_pagamento_prevista date,
    ADD COLUMN IF NOT EXISTS origem_ultima_oferta varchar(50),
    ADD COLUMN IF NOT EXISTS motivo_recusa_cliente text,
    ADD COLUMN IF NOT EXISTS proposito_emprestimo VARCHAR(255),
    ADD COLUMN IF NOT EXISTS estado_civil VARCHAR(50),
    ADD COLUMN IF NOT EXISTS possui_imovel_veiculo BOOLEAN,
    ADD COLUMN IF NOT EXISTS data_nascimento_solicitante date,
    ADD COLUMN IF NOT EXISTS num_parcelas_preferido integer;

ALTER TABLE schema_sjfjuristas.propostas_emprestimo
    ADD COLUMN IF NOT EXISTS remuneracao_mensal_solicitante NUMERIC(16, 2),
    ADD COLUMN IF NOT EXISTS ocupacao_id_ocupacoes UUID,
    ADD COLUMN IF NOT EXISTS outra_ocupacao_solicitante VARCHAR(255),
    ADD CONSTRAINT fk_propostas_ocupacoes FOREIGN KEY (ocupacao_id_ocupacoes) REFERENCES schema_sjfjuristas.ocupacoes(ocupacao_id) ON DELETE SET NULL;

CREATE TABLE schema_sjfjuristas.propostas_historico (
    historico_id uuid NOT NULL DEFAULT gen_random_uuid(),
    proposta_id_propostas_emprestimo uuid NOT NULL,
    data_alteracao timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
    ator_alteracao varchar(50) NOT NULL, -- 'ADMIN' ou 'CLIENTE'
    status_anterior varchar(100),
    status_novo varchar(100),
    valor_anterior numeric(16,2),
    valor_novo numeric(16,2),
    num_parcelas_anterior integer,
    num_parcelas_novo integer,
    taxa_juros_anterior numeric(16,4),
    taxa_juros_nova numeric(16,4),
    motivo_recusa text,
    observacoes text,
    PRIMARY KEY (historico_id),
    CONSTRAINT fk_historico_proposta FOREIGN KEY (proposta_id_propostas_emprestimo) REFERENCES schema_sjfjuristas.propostas_emprestimo(proposta_id) ON DELETE CASCADE
);