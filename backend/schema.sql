
    create table "s_sjfjuristas"."administradores" (
        ativo boolean default true,
        email_verificado boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        adminstrador_id uuid not null,
        "perfil_id_perfis_usuario" uuid,
        telefone_contato varchar(20),
        matricula_funcionario varchar(50),
        cargo_interno varchar(100),
        departamento varchar(100),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        primary key (adminstrador_id),
        constraint administradores_email_uq unique (email),
        constraint Administradores_matricula_funcionario_uq unique (matricula_funcionario)
    );

    create table "s_sjfjuristas"."chaves_pix_usuario" (
        ativa_para_desembolso boolean default false,
        "false" boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        chave_pix_id uuid not null,
        "tipo_chave_pix_id_tipos_chave_pix" uuid,
        "usuario_id_usuarios" uuid,
        valor_chave varchar(255) not null,
        primary key (chave_pix_id)
    );

    create table "s_sjfjuristas"."comprovantes_pagamento" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao_admin timestamp(6) with time zone,
        tamanho_bytes bigint,
        comprovante_id uuid not null,
        "pagamento_id_pagamentos_parcela" uuid,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        status_verificacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        nome_arquivo_original varchar(255),
        observacoes_verificacao text,
        url_comprovante text not null,
        primary key (comprovante_id)
    );

    create table "s_sjfjuristas"."configuracoes_sistema" (
        data_modificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        config_id uuid not null,
        grupo_config varchar(50),
        tipo_dado varchar(50) default 'TEXT',
        chave_config varchar(100) not null,
        descricao text,
        valor_config text,
        primary key (config_id),
        constraint ConfiguracoesSistema_chave_config_uq unique (chave_config)
    );

    create table "s_sjfjuristas"."documentos_proposta" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_validacao timestamp(6) with time zone,
        tamanho_bytes bigint,
        documento_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid,
        "tipo_documento_id_tipos_documento" uuid,
        "usuario_id_usuarios" uuid,
        status_validacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        hash_documento varchar(255),
        nome_arquivo_original varchar(255),
        observacoes_validacao text,
        url_documento text not null,
        primary key (documento_id)
    );

    create table "s_sjfjuristas"."emprestimos" (
        cet_anual numeric(16,6),
        data_inicio_cobranca_parcelas date not null,
        data_primeiro_vencimento date not null,
        data_ultimo_vencimento date not null,
        iof_valor numeric(16,2) default 0.00,
        numero_total_parcelas integer not null,
        outras_taxas numeric(16,2) default 0.00,
        saldo_devedor_atual numeric(16,2) not null,
        seguro_valor numeric(16,2) default 0.00,
        taxa_juros_diaria_efetiva numeric(16,2) not null,
        taxa_juros_mensal_efetiva numeric(16,2) not null,
        valor_contratado numeric(16,2) not null,
        valor_liberado numeric(16,2) not null,
        valor_parcela_diaria numeric(16,2) not null,
        data_contratacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_desembolso_efetivo timestamp(6) with time zone,
        data_solicitacao_desembolso timestamp(6) with time zone,
        "chave_pix_id_chaves_pix_usuario" uuid,
        emprestimo_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid unique,
        "status_emprestimo_id_status_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        id_transacao_desembolso_psp varchar(255),
        primary key (emprestimo_id),
        constraint emprestimo_id_transacao_desembolso_psp_uq unique (id_transacao_desembolso_psp)
    );

    create table "s_sjfjuristas"."log_auditoria" (
        sucesso boolean default true,
        timestamp_evento timestamp(6) with time zone default CURRENT_TIMESTAMP,
        log_id uuid not null,
        "usuario_id_usuarios" uuid,
        ip_origem varchar(45),
        perfil_usuario_no_evento varchar(50),
        acao_codigo varchar(100) not null,
        entidade_afetada varchar(100),
        id_entidade_afetada varchar(255),
        descricao_acao text,
        detalhes_antes jsonb,
        detalhes_depois jsonb,
        mensagem_erro text,
        user_agent text,
        primary key (log_id)
    );

    create table "s_sjfjuristas"."notificacoes" (
        enviada_por_push boolean default false,
        lida boolean default false,
        data_criacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_envio_email timestamp(6) with time zone,
        data_envio_push timestamp(6) with time zone,
        data_leitura timestamp(6) with time zone,
        notificacao_id uuid not null,
        "usuario_id_usuarios" uuid,
        tipo_notificacao varchar(50) not null,
        titulo varchar(255) not null,
        link_destino_app text,
        mensagem text not null,
        metadados jsonb,
        primary key (notificacao_id)
    );

    create table "s_sjfjuristas"."pagamentos_parcela" (
        valor_pago numeric(16,2) not null,
        data_confirmacao_manual timestamp(6) with time zone,
        data_pagamento_efetivo timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        "emprestimo_id_emprestimos" uuid,
        pagamento_id uuid not null,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        meio_pagamento varchar(50) default 'PIX',
        id_transacao_pagamento_psp varchar(255),
        observacoes text,
        webhook_payload_psp jsonb,
        primary key (pagamento_id),
        constraint PagamentosParcela_id_transacao_pagamento_psp_uq unique (id_transacao_pagamento_psp)
    );

    create table "s_sjfjuristas"."parcelas_emprestimo" (
        data_vencimento date not null,
        numero_parcela integer not null,
        valor_juros numeric(16,2) not null,
        valor_principal_amortizado numeric(16,2) not null,
        valor_total_parcela numeric(16,2) not null,
        data_geracao_pix timestamp(6) with time zone,
        "emprestimo_id_emprestimos" uuid,
        parcela_id uuid not null,
        "status_pagamento_parcela_id_status_pagamento_parcela" uuid,
        id_transacao_geracao_pix_psp varchar(255),
        pix_copia_cola text,
        pix_qr_code_base64 text,
        primary key (parcela_id)
    );

    create table "s_sjfjuristas"."perfis_usuario" (
        perfil_id uuid not null,
        nome_perfil varchar(50) not null,
        primary key (perfil_id),
        constraint PerfisUsuario_nome_perfil_uq unique (nome_perfil)
    );

    create table "s_sjfjuristas"."propostas_emprestimo" (
        link_app_enviado boolean default false,
        termos_aceitos_lp boolean default false not null,
        valor_solicitado numeric(16,2) not null,
        data_aceite_termos_lp timestamp(6) with time zone,
        data_analise timestamp(6) with time zone,
        data_envio_link_app timestamp(6) with time zone,
        data_solicitacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        cpf_solicitante varchar(14) not null,
        proposta_id uuid not null,
        "status_proposta_id_status_proposta" uuid,
        "usuario_id_usuarios" uuid,
        telefone_whatsapp_solicitante varchar(20),
        ip_solicitacao varchar(45),
        origem_captacao varchar(100) default 'LandingPage',
        email_solicitante varchar(255) not null,
        nome_completo_solicitante varchar(255) not null,
        motivo_negacao text,
        user_agent_solicitacao text,
        primary key (proposta_id)
    );

    create table "s_sjfjuristas"."status_emprestimo" (
        status_emprestimo_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_emprestimo_id),
        constraint StatusEmprestimo_nome_status_uq unique (nome_status)
    );

    create table "s_sjfjuristas"."status_pagamento_parcela" (
        status_pagamento_parcela_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_pagamento_parcela_id),
        constraint StatusPagamentoParcela_nome_status_uq unique (nome_status)
    );

    create table "s_sjfjuristas"."status_proposta" (
        status_proposta_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_proposta_id),
        constraint statusproposta_nome_status_uq unique (nome_status)
    );

    create table "s_sjfjuristas"."tipos_chave_pix" (
        tipo_chave_pix_id uuid not null,
        nome_tipo varchar(50) not null,
        primary key (tipo_chave_pix_id),
        constraint pix_nome_tipo_uq unique (nome_tipo)
    );

    create table "s_sjfjuristas"."tipos_documento" (
        obrigatorio_proposta boolean default true,
        tipo_documento_id uuid not null,
        nome_documento varchar(100) not null,
        descricao text,
        primary key (tipo_documento_id),
        constraint TiposDocumento_nome_documento_uq unique (nome_documento)
    );

    create table "s_sjfjuristas"."usuarios" (
        aceitou_termos_app boolean default false,
        ativo boolean default true,
        data_nascimento date,
        email_verificado boolean default false,
        data_aceite_termos_app timestamp(6) with time zone,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        cpf varchar(14),
        "perfil_id_perfis_usuario" uuid,
        usuario_id uuid not null,
        telefone_whatsapp varchar(20),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        endereco_completo text,
        primary key (usuario_id),
        constraint usuarios_cpf_uq unique (cpf),
        constraint usuarios_email_uq unique (email)
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_comprovantes_pagamento" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "comprovante_id_ComprovantesPagamento" uuid not null,
        "comprovante_id_comprovantes_pagamento" uuid not null,
        primary key ("adminstrador_id_Administradores", "comprovante_id_ComprovantesPagamento")
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_configuracoes_sistema" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "config_id_ConfiguracoesSistema" uuid not null,
        "config_id_configuracoes_sistema" uuid not null,
        primary key ("adminstrador_id_Administradores", "config_id_ConfiguracoesSistema")
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_pagamentos_parcela" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "pagamento_id_PagamentosParcela" uuid not null,
        "pagamento_id_pagamentos_parcela" uuid not null,
        primary key ("adminstrador_id_Administradores", "pagamento_id_PagamentosParcela")
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_propostas_emprestimo" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "proposta_id_PropostasEmprestimo" uuid not null,
        "proposta_id_propostas_emprestimo" uuid not null,
        primary key ("adminstrador_id_Administradores", "proposta_id_PropostasEmprestimo")
    );

    create table many_administradores_handle_many_comprovantes_pagamento (
        adminstrador_id_administradores uuid not null,
        comprovante_id_comprovantes_pagamento uuid not null,
        primary key (adminstrador_id_administradores, comprovante_id_comprovantes_pagamento)
    );

    create table many_administradores_handle_many_configuracoes_sistema (
        adminstrador_id_administradores uuid not null,
        config_id_configuracoes_sistema uuid not null,
        primary key (adminstrador_id_administradores, config_id_configuracoes_sistema)
    );

    create table many_administradores_handle_many_pagamentos_parcela (
        adminstrador_id_administradores uuid not null,
        pagamento_id_pagamentos_parcela uuid not null,
        primary key (adminstrador_id_administradores, pagamento_id_pagamentos_parcela)
    );

    create table many_administradores_handle_many_propostas_emprestimo (
        adminstrador_id_administradores uuid not null,
        proposta_id_propostas_emprestimo uuid not null,
        primary key (adminstrador_id_administradores, proposta_id_propostas_emprestimo)
    );

    alter table if exists "s_sjfjuristas"."chaves_pix_usuario" 
       add constraint FKm0epd74q4ih2nu40upcs6if4i 
       foreign key ("tipo_chave_pix_id_tipos_chave_pix") 
       references "s_sjfjuristas"."tipos_chave_pix" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."chaves_pix_usuario" 
       add constraint FK978n21bmv7hhqeuuh851bb8su 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."comprovantes_pagamento" 
       add constraint FKf3jjg9p5q2j8o1j5n8ted5imc 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references "s_sjfjuristas"."pagamentos_parcela" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."comprovantes_pagamento" 
       add constraint FKae3hauwqh8wvfm6771a6q5le5 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references "s_sjfjuristas"."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."comprovantes_pagamento" 
       add constraint FK41rb74urb7r51lnxq7rfl1br9 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."documentos_proposta" 
       add constraint FKjrrl2cktpj56a49x08c6eigft 
       foreign key ("proposta_id_propostas_emprestimo") 
       references "s_sjfjuristas"."propostas_emprestimo" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."documentos_proposta" 
       add constraint FKdsfsso76yplf0odxr0t4j8dnv 
       foreign key ("tipo_documento_id_tipos_documento") 
       references "s_sjfjuristas"."tipos_documento" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."documentos_proposta" 
       add constraint FKdkueq55gxbgmpxkh4fi5uwi3f 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKswjhsx9t4yqw8nk3ux1b5r52e 
       foreign key ("chave_pix_id_chaves_pix_usuario") 
       references "s_sjfjuristas"."chaves_pix_usuario" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKpn4lutoq9dqbq7css1x1pkovp 
       foreign key ("proposta_id_propostas_emprestimo") 
       references "s_sjfjuristas"."propostas_emprestimo" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKbhaps7jd9i79injgr3t9e7hlx 
       foreign key ("status_emprestimo_id_status_emprestimo") 
       references "s_sjfjuristas"."status_emprestimo" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKt02sc8ncbvto7f8fvd9r01a0g 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."log_auditoria" 
       add constraint FKorhjb0t9v9kepq2co3qwvfm1j 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."notificacoes" 
       add constraint FK1myl9mtefubpj5yf12f5m6fu1 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."pagamentos_parcela" 
       add constraint FKk3weydr8yxulctwrxhsqdj0mk 
       foreign key ("emprestimo_id_emprestimos") 
       references "s_sjfjuristas"."emprestimos" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."pagamentos_parcela" 
       add constraint FKcysqemkhebwfouislae1jlh41 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references "s_sjfjuristas"."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."pagamentos_parcela" 
       add constraint FK3wxqgty7dyru5w892h7etkm4c 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."parcelas_emprestimo" 
       add constraint FKeijl0ka019xxpiny7uwj6dcd9 
       foreign key ("emprestimo_id_emprestimos") 
       references "s_sjfjuristas"."emprestimos" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."parcelas_emprestimo" 
       add constraint FK4rs59n04vdtpxm336p4gr755o 
       foreign key ("status_pagamento_parcela_id_status_pagamento_parcela") 
       references "s_sjfjuristas"."status_pagamento_parcela" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."propostas_emprestimo" 
       add constraint FKcr4wyy7kuyvuoxdjdfbog2b9c 
       foreign key ("status_proposta_id_status_proposta") 
       references "s_sjfjuristas"."status_proposta" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."propostas_emprestimo" 
       add constraint FKrh8jhhd3n463id09uco07w0tg 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."usuarios" 
       add constraint FKmhivq4xcjsykmier7t7ko0wk4 
       foreign key ("perfil_id_perfis_usuario") 
       references "s_sjfjuristas"."perfis_usuario" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKpaww6dku2pkrh6eorbytiyf98 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKafw44arv0c2f363oh4mycpiw4 
       foreign key ("comprovante_id_comprovantes_pagamento") 
       references "s_sjfjuristas"."comprovantes_pagamento" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FKkb6tfteb0fqlf0gmfof5v9rdp 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FK82p1f96r3ogb8d6nbklfxf0gc 
       foreign key ("config_id_configuracoes_sistema") 
       references "s_sjfjuristas"."configuracoes_sistema" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKar10cb2hmx49aov1e9116wc47 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKj7ayjt804exkipsgo5eulekfx 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references "s_sjfjuristas"."pagamentos_parcela" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKtfmq4vyb36bftemd7d2kgv33l 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKkgmcbt6o68ognt7apmyn8fj10 
       foreign key ("proposta_id_propostas_emprestimo") 
       references "s_sjfjuristas"."propostas_emprestimo" 
       on delete set null;

    alter table if exists many_administradores_handle_many_comprovantes_pagamento 
       add constraint FKn0d3bt1iqh9ilgnx6gcw1t8cu 
       foreign key (comprovante_id_comprovantes_pagamento) 
       references "s_sjfjuristas"."comprovantes_pagamento";

    alter table if exists many_administradores_handle_many_comprovantes_pagamento 
       add constraint FK9wlfmfd4evi80pub4m5v7sxg1 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    alter table if exists many_administradores_handle_many_configuracoes_sistema 
       add constraint FKfehbdcvtxvgkc9yh59ku12nd0 
       foreign key (config_id_configuracoes_sistema) 
       references "s_sjfjuristas"."configuracoes_sistema";

    alter table if exists many_administradores_handle_many_configuracoes_sistema 
       add constraint FKan1id2w9fpegfw5nkvx7lec5k 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    alter table if exists many_administradores_handle_many_pagamentos_parcela 
       add constraint FK2t8cesiei5ah3d3y3w545ln2q 
       foreign key (pagamento_id_pagamentos_parcela) 
       references "s_sjfjuristas"."pagamentos_parcela";

    alter table if exists many_administradores_handle_many_pagamentos_parcela 
       add constraint FKthxdoser0m1ctwt6f6582faj7 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    alter table if exists many_administradores_handle_many_propostas_emprestimo 
       add constraint FK2d5c7iq4ra27ci1f423qr8nf4 
       foreign key (proposta_id_propostas_emprestimo) 
       references "s_sjfjuristas"."propostas_emprestimo";

    alter table if exists many_administradores_handle_many_propostas_emprestimo 
       add constraint FK8ef8h62vr3rbdndhpxafmm8t0 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    create table "s_sjfjuristas"."administradores" (
        ativo boolean default true,
        email_verificado boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        adminstrador_id uuid not null,
        "perfil_id_perfis_usuario" uuid,
        telefone_contato varchar(20),
        matricula_funcionario varchar(50),
        cargo_interno varchar(100),
        departamento varchar(100),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        primary key (adminstrador_id),
        constraint administradores_email_uq unique (email),
        constraint Administradores_matricula_funcionario_uq unique (matricula_funcionario)
    );

    create table "s_sjfjuristas"."chaves_pix_usuario" (
        ativa_para_desembolso boolean default false,
        "false" boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        chave_pix_id uuid not null,
        "tipo_chave_pix_id_tipos_chave_pix" uuid,
        "usuario_id_usuarios" uuid,
        valor_chave varchar(255) not null,
        primary key (chave_pix_id)
    );

    create table "s_sjfjuristas"."comprovantes_pagamento" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao_admin timestamp(6) with time zone,
        tamanho_bytes bigint,
        comprovante_id uuid not null,
        "pagamento_id_pagamentos_parcela" uuid,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        status_verificacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        nome_arquivo_original varchar(255),
        observacoes_verificacao text,
        url_comprovante text not null,
        primary key (comprovante_id)
    );

    create table "s_sjfjuristas"."configuracoes_sistema" (
        data_modificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        config_id uuid not null,
        grupo_config varchar(50),
        tipo_dado varchar(50) default 'TEXT',
        chave_config varchar(100) not null,
        descricao text,
        valor_config text,
        primary key (config_id),
        constraint ConfiguracoesSistema_chave_config_uq unique (chave_config)
    );

    create table "s_sjfjuristas"."documentos_proposta" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_validacao timestamp(6) with time zone,
        tamanho_bytes bigint,
        documento_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid,
        "tipo_documento_id_tipos_documento" uuid,
        "usuario_id_usuarios" uuid,
        status_validacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        hash_documento varchar(255),
        nome_arquivo_original varchar(255),
        observacoes_validacao text,
        url_documento text not null,
        primary key (documento_id)
    );

    create table "s_sjfjuristas"."emprestimos" (
        cet_anual numeric(16,6),
        data_inicio_cobranca_parcelas date not null,
        data_primeiro_vencimento date not null,
        data_ultimo_vencimento date not null,
        iof_valor numeric(16,2) default 0.00,
        numero_total_parcelas integer not null,
        outras_taxas numeric(16,2) default 0.00,
        saldo_devedor_atual numeric(16,2) not null,
        seguro_valor numeric(16,2) default 0.00,
        taxa_juros_diaria_efetiva numeric(16,2) not null,
        taxa_juros_mensal_efetiva numeric(16,2) not null,
        valor_contratado numeric(16,2) not null,
        valor_liberado numeric(16,2) not null,
        valor_parcela_diaria numeric(16,2) not null,
        data_contratacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_desembolso_efetivo timestamp(6) with time zone,
        data_solicitacao_desembolso timestamp(6) with time zone,
        "chave_pix_id_chaves_pix_usuario" uuid,
        emprestimo_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid unique,
        "status_emprestimo_id_status_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        id_transacao_desembolso_psp varchar(255),
        primary key (emprestimo_id),
        constraint emprestimo_id_transacao_desembolso_psp_uq unique (id_transacao_desembolso_psp)
    );

    create table "s_sjfjuristas"."log_auditoria" (
        sucesso boolean default true,
        timestamp_evento timestamp(6) with time zone default CURRENT_TIMESTAMP,
        log_id uuid not null,
        "usuario_id_usuarios" uuid,
        ip_origem varchar(45),
        perfil_usuario_no_evento varchar(50),
        acao_codigo varchar(100) not null,
        entidade_afetada varchar(100),
        id_entidade_afetada varchar(255),
        descricao_acao text,
        detalhes_antes jsonb,
        detalhes_depois jsonb,
        mensagem_erro text,
        user_agent text,
        primary key (log_id)
    );

    create table "s_sjfjuristas"."notificacoes" (
        enviada_por_push boolean default false,
        lida boolean default false,
        data_criacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_envio_email timestamp(6) with time zone,
        data_envio_push timestamp(6) with time zone,
        data_leitura timestamp(6) with time zone,
        notificacao_id uuid not null,
        "usuario_id_usuarios" uuid,
        tipo_notificacao varchar(50) not null,
        titulo varchar(255) not null,
        link_destino_app text,
        mensagem text not null,
        metadados jsonb,
        primary key (notificacao_id)
    );

    create table "s_sjfjuristas"."pagamentos_parcela" (
        valor_pago numeric(16,2) not null,
        data_confirmacao_manual timestamp(6) with time zone,
        data_pagamento_efetivo timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        "emprestimo_id_emprestimos" uuid,
        pagamento_id uuid not null,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        meio_pagamento varchar(50) default 'PIX',
        id_transacao_pagamento_psp varchar(255),
        observacoes text,
        webhook_payload_psp jsonb,
        primary key (pagamento_id),
        constraint PagamentosParcela_id_transacao_pagamento_psp_uq unique (id_transacao_pagamento_psp)
    );

    create table "s_sjfjuristas"."parcelas_emprestimo" (
        data_vencimento date not null,
        numero_parcela integer not null,
        valor_juros numeric(16,2) not null,
        valor_principal_amortizado numeric(16,2) not null,
        valor_total_parcela numeric(16,2) not null,
        data_geracao_pix timestamp(6) with time zone,
        "emprestimo_id_emprestimos" uuid,
        parcela_id uuid not null,
        "status_pagamento_parcela_id_status_pagamento_parcela" uuid,
        id_transacao_geracao_pix_psp varchar(255),
        pix_copia_cola text,
        pix_qr_code_base64 text,
        primary key (parcela_id)
    );

    create table "s_sjfjuristas"."perfis_usuario" (
        perfil_id uuid not null,
        nome_perfil varchar(50) not null,
        primary key (perfil_id),
        constraint PerfisUsuario_nome_perfil_uq unique (nome_perfil)
    );

    create table "s_sjfjuristas"."propostas_emprestimo" (
        link_app_enviado boolean default false,
        termos_aceitos_lp boolean default false not null,
        valor_solicitado numeric(16,2) not null,
        data_aceite_termos_lp timestamp(6) with time zone,
        data_analise timestamp(6) with time zone,
        data_envio_link_app timestamp(6) with time zone,
        data_solicitacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        cpf_solicitante varchar(14) not null,
        proposta_id uuid not null,
        "status_proposta_id_status_proposta" uuid,
        "usuario_id_usuarios" uuid,
        telefone_whatsapp_solicitante varchar(20),
        ip_solicitacao varchar(45),
        origem_captacao varchar(100) default 'LandingPage',
        email_solicitante varchar(255) not null,
        nome_completo_solicitante varchar(255) not null,
        motivo_negacao text,
        user_agent_solicitacao text,
        primary key (proposta_id)
    );

    create table "s_sjfjuristas"."status_emprestimo" (
        status_emprestimo_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_emprestimo_id),
        constraint StatusEmprestimo_nome_status_uq unique (nome_status)
    );

    create table "s_sjfjuristas"."status_pagamento_parcela" (
        status_pagamento_parcela_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_pagamento_parcela_id),
        constraint StatusPagamentoParcela_nome_status_uq unique (nome_status)
    );

    create table "s_sjfjuristas"."status_proposta" (
        status_proposta_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_proposta_id),
        constraint statusproposta_nome_status_uq unique (nome_status)
    );

    create table "s_sjfjuristas"."tipos_chave_pix" (
        tipo_chave_pix_id uuid not null,
        nome_tipo varchar(50) not null,
        primary key (tipo_chave_pix_id),
        constraint pix_nome_tipo_uq unique (nome_tipo)
    );

    create table "s_sjfjuristas"."tipos_documento" (
        obrigatorio_proposta boolean default true,
        tipo_documento_id uuid not null,
        nome_documento varchar(100) not null,
        descricao text,
        primary key (tipo_documento_id),
        constraint TiposDocumento_nome_documento_uq unique (nome_documento)
    );

    create table "s_sjfjuristas"."usuarios" (
        aceitou_termos_app boolean default false,
        ativo boolean default true,
        data_nascimento date,
        email_verificado boolean default false,
        data_aceite_termos_app timestamp(6) with time zone,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        cpf varchar(14),
        "perfil_id_perfis_usuario" uuid,
        usuario_id uuid not null,
        telefone_whatsapp varchar(20),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        endereco_completo text,
        primary key (usuario_id),
        constraint usuarios_cpf_uq unique (cpf),
        constraint usuarios_email_uq unique (email)
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_comprovantes_pagamento" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "comprovante_id_ComprovantesPagamento" uuid not null,
        "comprovante_id_comprovantes_pagamento" uuid not null,
        primary key ("adminstrador_id_Administradores", "comprovante_id_ComprovantesPagamento")
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_configuracoes_sistema" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "config_id_ConfiguracoesSistema" uuid not null,
        "config_id_configuracoes_sistema" uuid not null,
        primary key ("adminstrador_id_Administradores", "config_id_ConfiguracoesSistema")
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_pagamentos_parcela" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "pagamento_id_PagamentosParcela" uuid not null,
        "pagamento_id_pagamentos_parcela" uuid not null,
        primary key ("adminstrador_id_Administradores", "pagamento_id_PagamentosParcela")
    );

    create table "s_sjfjuristas"."many_administradores_handle_many_propostas_emprestimo" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "proposta_id_PropostasEmprestimo" uuid not null,
        "proposta_id_propostas_emprestimo" uuid not null,
        primary key ("adminstrador_id_Administradores", "proposta_id_PropostasEmprestimo")
    );

    create table many_administradores_handle_many_comprovantes_pagamento (
        adminstrador_id_administradores uuid not null,
        comprovante_id_comprovantes_pagamento uuid not null,
        primary key (adminstrador_id_administradores, comprovante_id_comprovantes_pagamento)
    );

    create table many_administradores_handle_many_configuracoes_sistema (
        adminstrador_id_administradores uuid not null,
        config_id_configuracoes_sistema uuid not null,
        primary key (adminstrador_id_administradores, config_id_configuracoes_sistema)
    );

    create table many_administradores_handle_many_pagamentos_parcela (
        adminstrador_id_administradores uuid not null,
        pagamento_id_pagamentos_parcela uuid not null,
        primary key (adminstrador_id_administradores, pagamento_id_pagamentos_parcela)
    );

    create table many_administradores_handle_many_propostas_emprestimo (
        adminstrador_id_administradores uuid not null,
        proposta_id_propostas_emprestimo uuid not null,
        primary key (adminstrador_id_administradores, proposta_id_propostas_emprestimo)
    );

    alter table if exists "s_sjfjuristas"."chaves_pix_usuario" 
       add constraint FKm0epd74q4ih2nu40upcs6if4i 
       foreign key ("tipo_chave_pix_id_tipos_chave_pix") 
       references "s_sjfjuristas"."tipos_chave_pix" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."chaves_pix_usuario" 
       add constraint FK978n21bmv7hhqeuuh851bb8su 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."comprovantes_pagamento" 
       add constraint FKf3jjg9p5q2j8o1j5n8ted5imc 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references "s_sjfjuristas"."pagamentos_parcela" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."comprovantes_pagamento" 
       add constraint FKae3hauwqh8wvfm6771a6q5le5 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references "s_sjfjuristas"."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."comprovantes_pagamento" 
       add constraint FK41rb74urb7r51lnxq7rfl1br9 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."documentos_proposta" 
       add constraint FKjrrl2cktpj56a49x08c6eigft 
       foreign key ("proposta_id_propostas_emprestimo") 
       references "s_sjfjuristas"."propostas_emprestimo" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."documentos_proposta" 
       add constraint FKdsfsso76yplf0odxr0t4j8dnv 
       foreign key ("tipo_documento_id_tipos_documento") 
       references "s_sjfjuristas"."tipos_documento" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."documentos_proposta" 
       add constraint FKdkueq55gxbgmpxkh4fi5uwi3f 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKswjhsx9t4yqw8nk3ux1b5r52e 
       foreign key ("chave_pix_id_chaves_pix_usuario") 
       references "s_sjfjuristas"."chaves_pix_usuario" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKpn4lutoq9dqbq7css1x1pkovp 
       foreign key ("proposta_id_propostas_emprestimo") 
       references "s_sjfjuristas"."propostas_emprestimo" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKbhaps7jd9i79injgr3t9e7hlx 
       foreign key ("status_emprestimo_id_status_emprestimo") 
       references "s_sjfjuristas"."status_emprestimo" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."emprestimos" 
       add constraint FKt02sc8ncbvto7f8fvd9r01a0g 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."log_auditoria" 
       add constraint FKorhjb0t9v9kepq2co3qwvfm1j 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."notificacoes" 
       add constraint FK1myl9mtefubpj5yf12f5m6fu1 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."pagamentos_parcela" 
       add constraint FKk3weydr8yxulctwrxhsqdj0mk 
       foreign key ("emprestimo_id_emprestimos") 
       references "s_sjfjuristas"."emprestimos" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."pagamentos_parcela" 
       add constraint FKcysqemkhebwfouislae1jlh41 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references "s_sjfjuristas"."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."pagamentos_parcela" 
       add constraint FK3wxqgty7dyru5w892h7etkm4c 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."parcelas_emprestimo" 
       add constraint FKeijl0ka019xxpiny7uwj6dcd9 
       foreign key ("emprestimo_id_emprestimos") 
       references "s_sjfjuristas"."emprestimos" 
       on delete cascade;

    alter table if exists "s_sjfjuristas"."parcelas_emprestimo" 
       add constraint FK4rs59n04vdtpxm336p4gr755o 
       foreign key ("status_pagamento_parcela_id_status_pagamento_parcela") 
       references "s_sjfjuristas"."status_pagamento_parcela" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."propostas_emprestimo" 
       add constraint FKcr4wyy7kuyvuoxdjdfbog2b9c 
       foreign key ("status_proposta_id_status_proposta") 
       references "s_sjfjuristas"."status_proposta" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."propostas_emprestimo" 
       add constraint FKrh8jhhd3n463id09uco07w0tg 
       foreign key ("usuario_id_usuarios") 
       references "s_sjfjuristas"."usuarios" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."usuarios" 
       add constraint FKmhivq4xcjsykmier7t7ko0wk4 
       foreign key ("perfil_id_perfis_usuario") 
       references "s_sjfjuristas"."perfis_usuario" 
       on delete restrict;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKpaww6dku2pkrh6eorbytiyf98 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKafw44arv0c2f363oh4mycpiw4 
       foreign key ("comprovante_id_comprovantes_pagamento") 
       references "s_sjfjuristas"."comprovantes_pagamento" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FKkb6tfteb0fqlf0gmfof5v9rdp 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FK82p1f96r3ogb8d6nbklfxf0gc 
       foreign key ("config_id_configuracoes_sistema") 
       references "s_sjfjuristas"."configuracoes_sistema" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKar10cb2hmx49aov1e9116wc47 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKj7ayjt804exkipsgo5eulekfx 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references "s_sjfjuristas"."pagamentos_parcela" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKtfmq4vyb36bftemd7d2kgv33l 
       foreign key ("adminstrador_id_administradores") 
       references "s_sjfjuristas"."administradores" 
       on delete set null;

    alter table if exists "s_sjfjuristas"."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKkgmcbt6o68ognt7apmyn8fj10 
       foreign key ("proposta_id_propostas_emprestimo") 
       references "s_sjfjuristas"."propostas_emprestimo" 
       on delete set null;

    alter table if exists many_administradores_handle_many_comprovantes_pagamento 
       add constraint FKn0d3bt1iqh9ilgnx6gcw1t8cu 
       foreign key (comprovante_id_comprovantes_pagamento) 
       references "s_sjfjuristas"."comprovantes_pagamento";

    alter table if exists many_administradores_handle_many_comprovantes_pagamento 
       add constraint FK9wlfmfd4evi80pub4m5v7sxg1 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    alter table if exists many_administradores_handle_many_configuracoes_sistema 
       add constraint FKfehbdcvtxvgkc9yh59ku12nd0 
       foreign key (config_id_configuracoes_sistema) 
       references "s_sjfjuristas"."configuracoes_sistema";

    alter table if exists many_administradores_handle_many_configuracoes_sistema 
       add constraint FKan1id2w9fpegfw5nkvx7lec5k 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    alter table if exists many_administradores_handle_many_pagamentos_parcela 
       add constraint FK2t8cesiei5ah3d3y3w545ln2q 
       foreign key (pagamento_id_pagamentos_parcela) 
       references "s_sjfjuristas"."pagamentos_parcela";

    alter table if exists many_administradores_handle_many_pagamentos_parcela 
       add constraint FKthxdoser0m1ctwt6f6582faj7 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    alter table if exists many_administradores_handle_many_propostas_emprestimo 
       add constraint FK2d5c7iq4ra27ci1f423qr8nf4 
       foreign key (proposta_id_propostas_emprestimo) 
       references "s_sjfjuristas"."propostas_emprestimo";

    alter table if exists many_administradores_handle_many_propostas_emprestimo 
       add constraint FK8ef8h62vr3rbdndhpxafmm8t0 
       foreign key (adminstrador_id_administradores) 
       references "s_sjfjuristas"."administradores";

    create table schema_sjfjuristas."administradores" (
        ativo boolean default true,
        email_verificado boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        adminstrador_id uuid not null,
        "perfil_id_perfis_usuario" uuid,
        telefone_contato varchar(20),
        matricula_funcionario varchar(50),
        cargo_interno varchar(100),
        departamento varchar(100),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        primary key (adminstrador_id),
        constraint administradores_email_uq unique (email),
        constraint Administradores_matricula_funcionario_uq unique (matricula_funcionario)
    );

    create table schema_sjfjuristas."chaves_pix_usuario" (
        ativa_para_desembolso boolean default false,
        "false" boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        chave_pix_id uuid not null,
        "tipo_chave_pix_id_tipos_chave_pix" uuid,
        "usuario_id_usuarios" uuid,
        valor_chave varchar(255) not null,
        primary key (chave_pix_id)
    );

    create table schema_sjfjuristas."comprovantes_pagamento" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao_admin timestamp(6) with time zone,
        tamanho_bytes bigint,
        comprovante_id uuid not null,
        "pagamento_id_pagamentos_parcela" uuid,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        status_verificacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        nome_arquivo_original varchar(255),
        observacoes_verificacao text,
        url_comprovante text not null,
        primary key (comprovante_id)
    );

    create table schema_sjfjuristas."configuracoes_sistema" (
        data_modificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        config_id uuid not null,
        grupo_config varchar(50),
        tipo_dado varchar(50) default 'TEXT',
        chave_config varchar(100) not null,
        descricao text,
        valor_config text,
        primary key (config_id),
        constraint ConfiguracoesSistema_chave_config_uq unique (chave_config)
    );

    create table schema_sjfjuristas."documentos_proposta" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_validacao timestamp(6) with time zone,
        tamanho_bytes bigint,
        documento_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid,
        "tipo_documento_id_tipos_documento" uuid,
        "usuario_id_usuarios" uuid,
        status_validacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        hash_documento varchar(255),
        nome_arquivo_original varchar(255),
        observacoes_validacao text,
        url_documento text not null,
        primary key (documento_id)
    );

    create table schema_sjfjuristas."emprestimos" (
        cet_anual numeric(16,6),
        data_inicio_cobranca_parcelas date not null,
        data_primeiro_vencimento date not null,
        data_ultimo_vencimento date not null,
        iof_valor numeric(16,2) default 0.00,
        numero_total_parcelas integer not null,
        outras_taxas numeric(16,2) default 0.00,
        saldo_devedor_atual numeric(16,2) not null,
        seguro_valor numeric(16,2) default 0.00,
        taxa_juros_diaria_efetiva numeric(16,2) not null,
        taxa_juros_mensal_efetiva numeric(16,2) not null,
        valor_contratado numeric(16,2) not null,
        valor_liberado numeric(16,2) not null,
        valor_parcela_diaria numeric(16,2) not null,
        data_contratacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_desembolso_efetivo timestamp(6) with time zone,
        data_solicitacao_desembolso timestamp(6) with time zone,
        "chave_pix_id_chaves_pix_usuario" uuid,
        emprestimo_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid unique,
        "status_emprestimo_id_status_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        id_transacao_desembolso_psp varchar(255),
        primary key (emprestimo_id),
        constraint emprestimo_id_transacao_desembolso_psp_uq unique (id_transacao_desembolso_psp)
    );

    create table schema_sjfjuristas."log_auditoria" (
        sucesso boolean default true,
        timestamp_evento timestamp(6) with time zone default CURRENT_TIMESTAMP,
        log_id uuid not null,
        "usuario_id_usuarios" uuid,
        ip_origem varchar(45),
        perfil_usuario_no_evento varchar(50),
        acao_codigo varchar(100) not null,
        entidade_afetada varchar(100),
        id_entidade_afetada varchar(255),
        descricao_acao text,
        detalhes_antes jsonb,
        detalhes_depois jsonb,
        mensagem_erro text,
        user_agent text,
        primary key (log_id)
    );

    create table schema_sjfjuristas."notificacoes" (
        enviada_por_push boolean default false,
        lida boolean default false,
        data_criacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_envio_email timestamp(6) with time zone,
        data_envio_push timestamp(6) with time zone,
        data_leitura timestamp(6) with time zone,
        notificacao_id uuid not null,
        "usuario_id_usuarios" uuid,
        tipo_notificacao varchar(50) not null,
        titulo varchar(255) not null,
        link_destino_app text,
        mensagem text not null,
        metadados jsonb,
        primary key (notificacao_id)
    );

    create table schema_sjfjuristas."pagamentos_parcela" (
        valor_pago numeric(16,2) not null,
        data_confirmacao_manual timestamp(6) with time zone,
        data_pagamento_efetivo timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        "emprestimo_id_emprestimos" uuid,
        pagamento_id uuid not null,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        meio_pagamento varchar(50) default 'PIX',
        id_transacao_pagamento_psp varchar(255),
        observacoes text,
        webhook_payload_psp jsonb,
        primary key (pagamento_id),
        constraint PagamentosParcela_id_transacao_pagamento_psp_uq unique (id_transacao_pagamento_psp)
    );

    create table schema_sjfjuristas."parcelas_emprestimo" (
        data_vencimento date not null,
        numero_parcela integer not null,
        valor_juros numeric(16,2) not null,
        valor_principal_amortizado numeric(16,2) not null,
        valor_total_parcela numeric(16,2) not null,
        data_geracao_pix timestamp(6) with time zone,
        "emprestimo_id_emprestimos" uuid,
        parcela_id uuid not null,
        "status_pagamento_parcela_id_status_pagamento_parcela" uuid,
        id_transacao_geracao_pix_psp varchar(255),
        pix_copia_cola text,
        pix_qr_code_base64 text,
        primary key (parcela_id)
    );

    create table schema_sjfjuristas."perfis_usuario" (
        perfil_id uuid not null,
        nome_perfil varchar(50) not null,
        primary key (perfil_id),
        constraint PerfisUsuario_nome_perfil_uq unique (nome_perfil)
    );

    create table schema_sjfjuristas."propostas_emprestimo" (
        link_app_enviado boolean default false,
        termos_aceitos_lp boolean default false not null,
        valor_solicitado numeric(16,2) not null,
        data_aceite_termos_lp timestamp(6) with time zone,
        data_analise timestamp(6) with time zone,
        data_envio_link_app timestamp(6) with time zone,
        data_solicitacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        cpf_solicitante varchar(14) not null,
        proposta_id uuid not null,
        "status_proposta_id_status_proposta" uuid,
        "usuario_id_usuarios" uuid,
        telefone_whatsapp_solicitante varchar(20),
        ip_solicitacao varchar(45),
        origem_captacao varchar(100) default 'LandingPage',
        email_solicitante varchar(255) not null,
        nome_completo_solicitante varchar(255) not null,
        motivo_negacao text,
        user_agent_solicitacao text,
        primary key (proposta_id)
    );

    create table schema_sjfjuristas."status_emprestimo" (
        status_emprestimo_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_emprestimo_id),
        constraint StatusEmprestimo_nome_status_uq unique (nome_status)
    );

    create table schema_sjfjuristas."status_pagamento_parcela" (
        status_pagamento_parcela_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_pagamento_parcela_id),
        constraint StatusPagamentoParcela_nome_status_uq unique (nome_status)
    );

    create table schema_sjfjuristas."status_proposta" (
        status_proposta_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_proposta_id),
        constraint statusproposta_nome_status_uq unique (nome_status)
    );

    create table schema_sjfjuristas."tipos_chave_pix" (
        tipo_chave_pix_id uuid not null,
        nome_tipo varchar(50) not null,
        primary key (tipo_chave_pix_id),
        constraint pix_nome_tipo_uq unique (nome_tipo)
    );

    create table schema_sjfjuristas."tipos_documento" (
        obrigatorio_proposta boolean default true,
        tipo_documento_id uuid not null,
        nome_documento varchar(100) not null,
        descricao text,
        primary key (tipo_documento_id),
        constraint TiposDocumento_nome_documento_uq unique (nome_documento)
    );

    create table schema_sjfjuristas."usuarios" (
        aceitou_termos_app boolean default false,
        ativo boolean default true,
        data_nascimento date,
        email_verificado boolean default false,
        data_aceite_termos_app timestamp(6) with time zone,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        cpf varchar(14),
        "perfil_id_perfis_usuario" uuid,
        usuario_id uuid not null,
        telefone_whatsapp varchar(20),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        endereco_completo text,
        primary key (usuario_id),
        constraint usuarios_cpf_uq unique (cpf),
        constraint usuarios_email_uq unique (email)
    );

    create table schema_sjfjuristas."many_administradores_handle_many_comprovantes_pagamento" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "comprovante_id_ComprovantesPagamento" uuid not null,
        "comprovante_id_comprovantes_pagamento" uuid not null,
        primary key ("adminstrador_id_Administradores", "comprovante_id_ComprovantesPagamento")
    );

    create table schema_sjfjuristas."many_administradores_handle_many_configuracoes_sistema" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "config_id_ConfiguracoesSistema" uuid not null,
        "config_id_configuracoes_sistema" uuid not null,
        primary key ("adminstrador_id_Administradores", "config_id_ConfiguracoesSistema")
    );

    create table schema_sjfjuristas."many_administradores_handle_many_pagamentos_parcela" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "pagamento_id_PagamentosParcela" uuid not null,
        "pagamento_id_pagamentos_parcela" uuid not null,
        primary key ("adminstrador_id_Administradores", "pagamento_id_PagamentosParcela")
    );

    create table schema_sjfjuristas."many_administradores_handle_many_propostas_emprestimo" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "proposta_id_PropostasEmprestimo" uuid not null,
        "proposta_id_propostas_emprestimo" uuid not null,
        primary key ("adminstrador_id_Administradores", "proposta_id_PropostasEmprestimo")
    );

    create table schema_sjfjuristas.many_administradores_handle_many_comprovantes_pagamento (
        adminstrador_id_administradores uuid not null,
        comprovante_id_comprovantes_pagamento uuid not null,
        primary key (adminstrador_id_administradores, comprovante_id_comprovantes_pagamento)
    );

    create table schema_sjfjuristas.many_administradores_handle_many_configuracoes_sistema (
        adminstrador_id_administradores uuid not null,
        config_id_configuracoes_sistema uuid not null,
        primary key (adminstrador_id_administradores, config_id_configuracoes_sistema)
    );

    create table schema_sjfjuristas.many_administradores_handle_many_pagamentos_parcela (
        adminstrador_id_administradores uuid not null,
        pagamento_id_pagamentos_parcela uuid not null,
        primary key (adminstrador_id_administradores, pagamento_id_pagamentos_parcela)
    );

    create table schema_sjfjuristas.many_administradores_handle_many_propostas_emprestimo (
        adminstrador_id_administradores uuid not null,
        proposta_id_propostas_emprestimo uuid not null,
        primary key (adminstrador_id_administradores, proposta_id_propostas_emprestimo)
    );

    alter table if exists schema_sjfjuristas."chaves_pix_usuario" 
       add constraint FKm0epd74q4ih2nu40upcs6if4i 
       foreign key ("tipo_chave_pix_id_tipos_chave_pix") 
       references schema_sjfjuristas."tipos_chave_pix" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."chaves_pix_usuario" 
       add constraint FK978n21bmv7hhqeuuh851bb8su 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."comprovantes_pagamento" 
       add constraint FKf3jjg9p5q2j8o1j5n8ted5imc 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references schema_sjfjuristas."pagamentos_parcela" 
       on delete set null;

    alter table if exists schema_sjfjuristas."comprovantes_pagamento" 
       add constraint FKae3hauwqh8wvfm6771a6q5le5 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references schema_sjfjuristas."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."comprovantes_pagamento" 
       add constraint FK41rb74urb7r51lnxq7rfl1br9 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."documentos_proposta" 
       add constraint FKjrrl2cktpj56a49x08c6eigft 
       foreign key ("proposta_id_propostas_emprestimo") 
       references schema_sjfjuristas."propostas_emprestimo" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."documentos_proposta" 
       add constraint FKdsfsso76yplf0odxr0t4j8dnv 
       foreign key ("tipo_documento_id_tipos_documento") 
       references schema_sjfjuristas."tipos_documento" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."documentos_proposta" 
       add constraint FKdkueq55gxbgmpxkh4fi5uwi3f 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKswjhsx9t4yqw8nk3ux1b5r52e 
       foreign key ("chave_pix_id_chaves_pix_usuario") 
       references schema_sjfjuristas."chaves_pix_usuario" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKpn4lutoq9dqbq7css1x1pkovp 
       foreign key ("proposta_id_propostas_emprestimo") 
       references schema_sjfjuristas."propostas_emprestimo" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKbhaps7jd9i79injgr3t9e7hlx 
       foreign key ("status_emprestimo_id_status_emprestimo") 
       references schema_sjfjuristas."status_emprestimo" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKt02sc8ncbvto7f8fvd9r01a0g 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."log_auditoria" 
       add constraint FKorhjb0t9v9kepq2co3qwvfm1j 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."notificacoes" 
       add constraint FK1myl9mtefubpj5yf12f5m6fu1 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."pagamentos_parcela" 
       add constraint FKk3weydr8yxulctwrxhsqdj0mk 
       foreign key ("emprestimo_id_emprestimos") 
       references schema_sjfjuristas."emprestimos" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."pagamentos_parcela" 
       add constraint FKcysqemkhebwfouislae1jlh41 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references schema_sjfjuristas."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."pagamentos_parcela" 
       add constraint FK3wxqgty7dyru5w892h7etkm4c 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."parcelas_emprestimo" 
       add constraint FKeijl0ka019xxpiny7uwj6dcd9 
       foreign key ("emprestimo_id_emprestimos") 
       references schema_sjfjuristas."emprestimos" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."parcelas_emprestimo" 
       add constraint FK4rs59n04vdtpxm336p4gr755o 
       foreign key ("status_pagamento_parcela_id_status_pagamento_parcela") 
       references schema_sjfjuristas."status_pagamento_parcela" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."propostas_emprestimo" 
       add constraint FKcr4wyy7kuyvuoxdjdfbog2b9c 
       foreign key ("status_proposta_id_status_proposta") 
       references schema_sjfjuristas."status_proposta" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."propostas_emprestimo" 
       add constraint FKrh8jhhd3n463id09uco07w0tg 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."usuarios" 
       add constraint FKmhivq4xcjsykmier7t7ko0wk4 
       foreign key ("perfil_id_perfis_usuario") 
       references schema_sjfjuristas."perfis_usuario" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKpaww6dku2pkrh6eorbytiyf98 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKafw44arv0c2f363oh4mycpiw4 
       foreign key ("comprovante_id_comprovantes_pagamento") 
       references schema_sjfjuristas."comprovantes_pagamento" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FKkb6tfteb0fqlf0gmfof5v9rdp 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FK82p1f96r3ogb8d6nbklfxf0gc 
       foreign key ("config_id_configuracoes_sistema") 
       references schema_sjfjuristas."configuracoes_sistema" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKar10cb2hmx49aov1e9116wc47 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKj7ayjt804exkipsgo5eulekfx 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references schema_sjfjuristas."pagamentos_parcela" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKtfmq4vyb36bftemd7d2kgv33l 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKkgmcbt6o68ognt7apmyn8fj10 
       foreign key ("proposta_id_propostas_emprestimo") 
       references schema_sjfjuristas."propostas_emprestimo" 
       on delete set null;

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_comprovantes_pagamento 
       add constraint FKn0d3bt1iqh9ilgnx6gcw1t8cu 
       foreign key (comprovante_id_comprovantes_pagamento) 
       references schema_sjfjuristas."comprovantes_pagamento";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_comprovantes_pagamento 
       add constraint FK9wlfmfd4evi80pub4m5v7sxg1 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_configuracoes_sistema 
       add constraint FKfehbdcvtxvgkc9yh59ku12nd0 
       foreign key (config_id_configuracoes_sistema) 
       references schema_sjfjuristas."configuracoes_sistema";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_configuracoes_sistema 
       add constraint FKan1id2w9fpegfw5nkvx7lec5k 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_pagamentos_parcela 
       add constraint FK2t8cesiei5ah3d3y3w545ln2q 
       foreign key (pagamento_id_pagamentos_parcela) 
       references schema_sjfjuristas."pagamentos_parcela";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_pagamentos_parcela 
       add constraint FKthxdoser0m1ctwt6f6582faj7 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_propostas_emprestimo 
       add constraint FK2d5c7iq4ra27ci1f423qr8nf4 
       foreign key (proposta_id_propostas_emprestimo) 
       references schema_sjfjuristas."propostas_emprestimo";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_propostas_emprestimo 
       add constraint FK8ef8h62vr3rbdndhpxafmm8t0 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";

    create table schema_sjfjuristas."administradores" (
        ativo boolean default true,
        email_verificado boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        adminstrador_id uuid not null,
        "perfil_id_perfis_usuario" uuid,
        telefone_contato varchar(20),
        matricula_funcionario varchar(50),
        cargo_interno varchar(100),
        departamento varchar(100),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        primary key (adminstrador_id),
        constraint administradores_email_uq unique (email),
        constraint Administradores_matricula_funcionario_uq unique (matricula_funcionario)
    );

    create table schema_sjfjuristas."chaves_pix_usuario" (
        ativa_para_desembolso boolean default false,
        "false" boolean default false,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        chave_pix_id uuid not null,
        "tipo_chave_pix_id_tipos_chave_pix" uuid,
        "usuario_id_usuarios" uuid,
        valor_chave varchar(255) not null,
        primary key (chave_pix_id)
    );

    create table schema_sjfjuristas."comprovantes_pagamento" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_verificacao_admin timestamp(6) with time zone,
        tamanho_bytes bigint,
        comprovante_id uuid not null,
        "pagamento_id_pagamentos_parcela" uuid,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        status_verificacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        nome_arquivo_original varchar(255),
        observacoes_verificacao text,
        url_comprovante text not null,
        primary key (comprovante_id)
    );

    create table schema_sjfjuristas."configuracoes_sistema" (
        data_modificacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        config_id uuid not null,
        grupo_config varchar(50),
        tipo_dado varchar(50) default 'TEXT',
        chave_config varchar(100) not null,
        descricao text,
        valor_config text,
        primary key (config_id),
        constraint ConfiguracoesSistema_chave_config_uq unique (chave_config)
    );

    create table schema_sjfjuristas."documentos_proposta" (
        data_upload timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_validacao timestamp(6) with time zone,
        tamanho_bytes bigint,
        documento_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid,
        "tipo_documento_id_tipos_documento" uuid,
        "usuario_id_usuarios" uuid,
        status_validacao varchar(50) default 'Pendente',
        tipo_mime varchar(100),
        hash_documento varchar(255),
        nome_arquivo_original varchar(255),
        observacoes_validacao text,
        url_documento text not null,
        primary key (documento_id)
    );

    create table schema_sjfjuristas."emprestimos" (
        cet_anual numeric(16,6),
        data_inicio_cobranca_parcelas date not null,
        data_primeiro_vencimento date not null,
        data_ultimo_vencimento date not null,
        iof_valor numeric(16,2) default 0.00,
        numero_total_parcelas integer not null,
        outras_taxas numeric(16,2) default 0.00,
        saldo_devedor_atual numeric(16,2) not null,
        seguro_valor numeric(16,2) default 0.00,
        taxa_juros_diaria_efetiva numeric(16,2) not null,
        taxa_juros_mensal_efetiva numeric(16,2) not null,
        valor_contratado numeric(16,2) not null,
        valor_liberado numeric(16,2) not null,
        valor_parcela_diaria numeric(16,2) not null,
        data_contratacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_desembolso_efetivo timestamp(6) with time zone,
        data_solicitacao_desembolso timestamp(6) with time zone,
        "chave_pix_id_chaves_pix_usuario" uuid,
        emprestimo_id uuid not null,
        "proposta_id_propostas_emprestimo" uuid unique,
        "status_emprestimo_id_status_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        id_transacao_desembolso_psp varchar(255),
        primary key (emprestimo_id),
        constraint emprestimo_id_transacao_desembolso_psp_uq unique (id_transacao_desembolso_psp)
    );

    create table schema_sjfjuristas."log_auditoria" (
        sucesso boolean default true,
        timestamp_evento timestamp(6) with time zone default CURRENT_TIMESTAMP,
        log_id uuid not null,
        "usuario_id_usuarios" uuid,
        ip_origem varchar(45),
        perfil_usuario_no_evento varchar(50),
        acao_codigo varchar(100) not null,
        entidade_afetada varchar(100),
        id_entidade_afetada varchar(255),
        descricao_acao text,
        detalhes_antes jsonb,
        detalhes_depois jsonb,
        mensagem_erro text,
        user_agent text,
        primary key (log_id)
    );

    create table schema_sjfjuristas."notificacoes" (
        enviada_por_push boolean default false,
        lida boolean default false,
        data_criacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        data_envio_email timestamp(6) with time zone,
        data_envio_push timestamp(6) with time zone,
        data_leitura timestamp(6) with time zone,
        notificacao_id uuid not null,
        "usuario_id_usuarios" uuid,
        tipo_notificacao varchar(50) not null,
        titulo varchar(255) not null,
        link_destino_app text,
        mensagem text not null,
        metadados jsonb,
        primary key (notificacao_id)
    );

    create table schema_sjfjuristas."pagamentos_parcela" (
        valor_pago numeric(16,2) not null,
        data_confirmacao_manual timestamp(6) with time zone,
        data_pagamento_efetivo timestamp(6) with time zone default CURRENT_TIMESTAMP not null,
        "emprestimo_id_emprestimos" uuid,
        pagamento_id uuid not null,
        "parcela_id_parcelas_emprestimo" uuid,
        "usuario_id_usuarios" uuid,
        meio_pagamento varchar(50) default 'PIX',
        id_transacao_pagamento_psp varchar(255),
        observacoes text,
        webhook_payload_psp jsonb,
        primary key (pagamento_id),
        constraint PagamentosParcela_id_transacao_pagamento_psp_uq unique (id_transacao_pagamento_psp)
    );

    create table schema_sjfjuristas."parcelas_emprestimo" (
        data_vencimento date not null,
        numero_parcela integer not null,
        valor_juros numeric(16,2) not null,
        valor_principal_amortizado numeric(16,2) not null,
        valor_total_parcela numeric(16,2) not null,
        data_geracao_pix timestamp(6) with time zone,
        "emprestimo_id_emprestimos" uuid,
        parcela_id uuid not null,
        "status_pagamento_parcela_id_status_pagamento_parcela" uuid,
        id_transacao_geracao_pix_psp varchar(255),
        pix_copia_cola text,
        pix_qr_code_base64 text,
        primary key (parcela_id)
    );

    create table schema_sjfjuristas."perfis_usuario" (
        perfil_id uuid not null,
        nome_perfil varchar(50) not null,
        primary key (perfil_id),
        constraint PerfisUsuario_nome_perfil_uq unique (nome_perfil)
    );

    create table schema_sjfjuristas."propostas_emprestimo" (
        link_app_enviado boolean default false,
        termos_aceitos_lp boolean default false not null,
        valor_solicitado numeric(16,2) not null,
        data_aceite_termos_lp timestamp(6) with time zone,
        data_analise timestamp(6) with time zone,
        data_envio_link_app timestamp(6) with time zone,
        data_solicitacao timestamp(6) with time zone default CURRENT_TIMESTAMP,
        cpf_solicitante varchar(14) not null,
        proposta_id uuid not null,
        "status_proposta_id_status_proposta" uuid,
        "usuario_id_usuarios" uuid,
        telefone_whatsapp_solicitante varchar(20),
        ip_solicitacao varchar(45),
        origem_captacao varchar(100) default 'LandingPage',
        email_solicitante varchar(255) not null,
        nome_completo_solicitante varchar(255) not null,
        motivo_negacao text,
        user_agent_solicitacao text,
        primary key (proposta_id)
    );

    create table schema_sjfjuristas."status_emprestimo" (
        status_emprestimo_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_emprestimo_id),
        constraint StatusEmprestimo_nome_status_uq unique (nome_status)
    );

    create table schema_sjfjuristas."status_pagamento_parcela" (
        status_pagamento_parcela_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_pagamento_parcela_id),
        constraint StatusPagamentoParcela_nome_status_uq unique (nome_status)
    );

    create table schema_sjfjuristas."status_proposta" (
        status_proposta_id uuid not null,
        nome_status varchar(50) not null,
        primary key (status_proposta_id),
        constraint statusproposta_nome_status_uq unique (nome_status)
    );

    create table schema_sjfjuristas."tipos_chave_pix" (
        tipo_chave_pix_id uuid not null,
        nome_tipo varchar(50) not null,
        primary key (tipo_chave_pix_id),
        constraint pix_nome_tipo_uq unique (nome_tipo)
    );

    create table schema_sjfjuristas."tipos_documento" (
        obrigatorio_proposta boolean default true,
        tipo_documento_id uuid not null,
        nome_documento varchar(100) not null,
        descricao text,
        primary key (tipo_documento_id),
        constraint TiposDocumento_nome_documento_uq unique (nome_documento)
    );

    create table schema_sjfjuristas."usuarios" (
        aceitou_termos_app boolean default false,
        ativo boolean default true,
        data_nascimento date,
        email_verificado boolean default false,
        data_aceite_termos_app timestamp(6) with time zone,
        data_cadastro timestamp(6) with time zone default CURRENT_TIMESTAMP,
        ultimo_login timestamp(6) with time zone,
        validade_token_recuperacao timestamp(6) with time zone,
        cpf varchar(14),
        "perfil_id_perfis_usuario" uuid,
        usuario_id uuid not null,
        telefone_whatsapp varchar(20),
        email varchar(255) not null,
        hash_senha varchar(255),
        nome_completo varchar(255),
        token_recuperacao_senha varchar(255),
        token_verificacao_email varchar(255),
        endereco_completo text,
        primary key (usuario_id),
        constraint usuarios_cpf_uq unique (cpf),
        constraint usuarios_email_uq unique (email)
    );

    create table schema_sjfjuristas."many_administradores_handle_many_comprovantes_pagamento" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "comprovante_id_ComprovantesPagamento" uuid not null,
        "comprovante_id_comprovantes_pagamento" uuid not null,
        primary key ("adminstrador_id_Administradores", "comprovante_id_ComprovantesPagamento")
    );

    create table schema_sjfjuristas."many_administradores_handle_many_configuracoes_sistema" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "config_id_ConfiguracoesSistema" uuid not null,
        "config_id_configuracoes_sistema" uuid not null,
        primary key ("adminstrador_id_Administradores", "config_id_ConfiguracoesSistema")
    );

    create table schema_sjfjuristas."many_administradores_handle_many_pagamentos_parcela" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "pagamento_id_PagamentosParcela" uuid not null,
        "pagamento_id_pagamentos_parcela" uuid not null,
        primary key ("adminstrador_id_Administradores", "pagamento_id_PagamentosParcela")
    );

    create table schema_sjfjuristas."many_administradores_handle_many_propostas_emprestimo" (
        "adminstrador_id_Administradores" uuid not null,
        "adminstrador_id_administradores" uuid not null,
        "proposta_id_PropostasEmprestimo" uuid not null,
        "proposta_id_propostas_emprestimo" uuid not null,
        primary key ("adminstrador_id_Administradores", "proposta_id_PropostasEmprestimo")
    );

    create table schema_sjfjuristas.many_administradores_handle_many_comprovantes_pagamento (
        adminstrador_id_administradores uuid not null,
        comprovante_id_comprovantes_pagamento uuid not null,
        primary key (adminstrador_id_administradores, comprovante_id_comprovantes_pagamento)
    );

    create table schema_sjfjuristas.many_administradores_handle_many_configuracoes_sistema (
        adminstrador_id_administradores uuid not null,
        config_id_configuracoes_sistema uuid not null,
        primary key (adminstrador_id_administradores, config_id_configuracoes_sistema)
    );

    create table schema_sjfjuristas.many_administradores_handle_many_pagamentos_parcela (
        adminstrador_id_administradores uuid not null,
        pagamento_id_pagamentos_parcela uuid not null,
        primary key (adminstrador_id_administradores, pagamento_id_pagamentos_parcela)
    );

    create table schema_sjfjuristas.many_administradores_handle_many_propostas_emprestimo (
        adminstrador_id_administradores uuid not null,
        proposta_id_propostas_emprestimo uuid not null,
        primary key (adminstrador_id_administradores, proposta_id_propostas_emprestimo)
    );

    alter table if exists schema_sjfjuristas."chaves_pix_usuario" 
       add constraint FKm0epd74q4ih2nu40upcs6if4i 
       foreign key ("tipo_chave_pix_id_tipos_chave_pix") 
       references schema_sjfjuristas."tipos_chave_pix" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."chaves_pix_usuario" 
       add constraint FK978n21bmv7hhqeuuh851bb8su 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."comprovantes_pagamento" 
       add constraint FKf3jjg9p5q2j8o1j5n8ted5imc 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references schema_sjfjuristas."pagamentos_parcela" 
       on delete set null;

    alter table if exists schema_sjfjuristas."comprovantes_pagamento" 
       add constraint FKae3hauwqh8wvfm6771a6q5le5 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references schema_sjfjuristas."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."comprovantes_pagamento" 
       add constraint FK41rb74urb7r51lnxq7rfl1br9 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."documentos_proposta" 
       add constraint FKjrrl2cktpj56a49x08c6eigft 
       foreign key ("proposta_id_propostas_emprestimo") 
       references schema_sjfjuristas."propostas_emprestimo" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."documentos_proposta" 
       add constraint FKdsfsso76yplf0odxr0t4j8dnv 
       foreign key ("tipo_documento_id_tipos_documento") 
       references schema_sjfjuristas."tipos_documento" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."documentos_proposta" 
       add constraint FKdkueq55gxbgmpxkh4fi5uwi3f 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKswjhsx9t4yqw8nk3ux1b5r52e 
       foreign key ("chave_pix_id_chaves_pix_usuario") 
       references schema_sjfjuristas."chaves_pix_usuario" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKpn4lutoq9dqbq7css1x1pkovp 
       foreign key ("proposta_id_propostas_emprestimo") 
       references schema_sjfjuristas."propostas_emprestimo" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKbhaps7jd9i79injgr3t9e7hlx 
       foreign key ("status_emprestimo_id_status_emprestimo") 
       references schema_sjfjuristas."status_emprestimo" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."emprestimos" 
       add constraint FKt02sc8ncbvto7f8fvd9r01a0g 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."log_auditoria" 
       add constraint FKorhjb0t9v9kepq2co3qwvfm1j 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."notificacoes" 
       add constraint FK1myl9mtefubpj5yf12f5m6fu1 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."pagamentos_parcela" 
       add constraint FKk3weydr8yxulctwrxhsqdj0mk 
       foreign key ("emprestimo_id_emprestimos") 
       references schema_sjfjuristas."emprestimos" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."pagamentos_parcela" 
       add constraint FKcysqemkhebwfouislae1jlh41 
       foreign key ("parcela_id_parcelas_emprestimo") 
       references schema_sjfjuristas."parcelas_emprestimo" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."pagamentos_parcela" 
       add constraint FK3wxqgty7dyru5w892h7etkm4c 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."parcelas_emprestimo" 
       add constraint FKeijl0ka019xxpiny7uwj6dcd9 
       foreign key ("emprestimo_id_emprestimos") 
       references schema_sjfjuristas."emprestimos" 
       on delete cascade;

    alter table if exists schema_sjfjuristas."parcelas_emprestimo" 
       add constraint FK4rs59n04vdtpxm336p4gr755o 
       foreign key ("status_pagamento_parcela_id_status_pagamento_parcela") 
       references schema_sjfjuristas."status_pagamento_parcela" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."propostas_emprestimo" 
       add constraint FKcr4wyy7kuyvuoxdjdfbog2b9c 
       foreign key ("status_proposta_id_status_proposta") 
       references schema_sjfjuristas."status_proposta" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."propostas_emprestimo" 
       add constraint FKrh8jhhd3n463id09uco07w0tg 
       foreign key ("usuario_id_usuarios") 
       references schema_sjfjuristas."usuarios" 
       on delete set null;

    alter table if exists schema_sjfjuristas."usuarios" 
       add constraint FKmhivq4xcjsykmier7t7ko0wk4 
       foreign key ("perfil_id_perfis_usuario") 
       references schema_sjfjuristas."perfis_usuario" 
       on delete restrict;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKpaww6dku2pkrh6eorbytiyf98 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_comprovantes_pagamento" 
       add constraint FKafw44arv0c2f363oh4mycpiw4 
       foreign key ("comprovante_id_comprovantes_pagamento") 
       references schema_sjfjuristas."comprovantes_pagamento" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FKkb6tfteb0fqlf0gmfof5v9rdp 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_configuracoes_sistema" 
       add constraint FK82p1f96r3ogb8d6nbklfxf0gc 
       foreign key ("config_id_configuracoes_sistema") 
       references schema_sjfjuristas."configuracoes_sistema" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKar10cb2hmx49aov1e9116wc47 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_pagamentos_parcela" 
       add constraint FKj7ayjt804exkipsgo5eulekfx 
       foreign key ("pagamento_id_pagamentos_parcela") 
       references schema_sjfjuristas."pagamentos_parcela" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKtfmq4vyb36bftemd7d2kgv33l 
       foreign key ("adminstrador_id_administradores") 
       references schema_sjfjuristas."administradores" 
       on delete set null;

    alter table if exists schema_sjfjuristas."many_administradores_handle_many_propostas_emprestimo" 
       add constraint FKkgmcbt6o68ognt7apmyn8fj10 
       foreign key ("proposta_id_propostas_emprestimo") 
       references schema_sjfjuristas."propostas_emprestimo" 
       on delete set null;

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_comprovantes_pagamento 
       add constraint FKn0d3bt1iqh9ilgnx6gcw1t8cu 
       foreign key (comprovante_id_comprovantes_pagamento) 
       references schema_sjfjuristas."comprovantes_pagamento";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_comprovantes_pagamento 
       add constraint FK9wlfmfd4evi80pub4m5v7sxg1 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_configuracoes_sistema 
       add constraint FKfehbdcvtxvgkc9yh59ku12nd0 
       foreign key (config_id_configuracoes_sistema) 
       references schema_sjfjuristas."configuracoes_sistema";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_configuracoes_sistema 
       add constraint FKan1id2w9fpegfw5nkvx7lec5k 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_pagamentos_parcela 
       add constraint FK2t8cesiei5ah3d3y3w545ln2q 
       foreign key (pagamento_id_pagamentos_parcela) 
       references schema_sjfjuristas."pagamentos_parcela";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_pagamentos_parcela 
       add constraint FKthxdoser0m1ctwt6f6582faj7 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_propostas_emprestimo 
       add constraint FK2d5c7iq4ra27ci1f423qr8nf4 
       foreign key (proposta_id_propostas_emprestimo) 
       references schema_sjfjuristas."propostas_emprestimo";

    alter table if exists schema_sjfjuristas.many_administradores_handle_many_propostas_emprestimo 
       add constraint FK8ef8h62vr3rbdndhpxafmm8t0 
       foreign key (adminstrador_id_administradores) 
       references schema_sjfjuristas."administradores";
