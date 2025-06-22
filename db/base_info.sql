-- Inserção de dados na tabela: s_sjfjuristas.tipos_chave_pix
INSERT INTO "schema_sjfjuristas"."tipos_chave_pix" (tipo_chave_pix_id, nome_tipo) VALUES
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51', 'CPF'),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a52', 'CNPJ'),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a53', 'E-mail'),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54', 'Telefone'),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'Chave Aleatória');

-- Inserção de dados na tabela: s_sjfjuristas.tipos_documento
INSERT INTO "schema_sjfjuristas"."tipos_documento" (obrigatorio_proposta, tipo_documento_id, nome_documento, descricao) VALUES
(true, 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', 'RG/CNH (Frente)', 'Foto da parte frontal do documento de identidade com foto (RG ou CNH).'),
(true, 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', 'RG/CNH (Verso)', 'Foto da parte de trás do documento de identidade com foto (RG ou CNH).'),
(true, 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a63', 'Comprovante de Residência', 'Conta de consumo recente (água, luz, telefone) ou outro documento válido como comprovante de endereço.'),
(false, 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a64', 'Selfie de Validação', 'Uma selfie do usuário segurando o documento de identidade ao lado do rosto.');

-- Inserção de dados na tabela: s_sjfjuristas.status_proposta
INSERT INTO "schema_sjfjuristas"."status_proposta" (status_proposta_id, nome_status) VALUES
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'Pendente de Análise'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Documentação Pendente'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', 'Em Análise'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'Aprovada'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', 'Negada'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26', 'Finalizada');

-- Inserção de dados na tabela: s_sjfjuristas.status_pagamento_parcela
INSERT INTO "schema_sjfjuristas"."status_pagamento_parcela" (status_pagamento_parcela_id, nome_status) VALUES
('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41', 'Pendente'),
('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a42', 'Pago'),
('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a43', 'Atrasado'),
('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44', 'Pago com Atraso');

-- Inserção de dados na tabela: s_sjfjuristas.status_emprestimo
INSERT INTO "schema_sjfjuristas"."status_emprestimo" (status_emprestimo_id, nome_status) VALUES
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a31', 'Pendente Desembolso'),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a32', 'Ativo'),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33', 'Finalizado'),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a34', 'Inadimplente'),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a35', 'Cancelado');

-- Inserção de dados na tabela: s_sjfjuristas.perfis_usuario
INSERT INTO "schema_sjfjuristas"."perfis_usuario" (perfil_id, nome_perfil) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Cliente'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Administrador');

ALTER TABLE schema_sjfjuristas.propostas_emprestimo
    ADD COLUMN IF NOT EXISTS valor_ofertado numeric(16,2),
    ADD COLUMN IF NOT EXISTS taxa_juros_ofertada numeric(16,4),
    ADD COLUMN IF NOT EXISTS num_parcelas_ofertado integer,
    ADD COLUMN IF NOT EXISTS data_deposito_prevista date,
    ADD COLUMN IF NOT EXISTS data_inicio_pagamento_prevista date,
    ADD COLUMN IF NOT EXISTS origem_ultima_oferta varchar(50),
    ADD COLUMN IF NOT EXISTS motivo_recusa_cliente text;

-- Adiciona os novos status necessários para o fluxo de negociação.
-- A cláusula "ON CONFLICT (nome_status) DO NOTHING" impede erros de chave duplicada
-- caso os status já existam na tabela.
INSERT INTO schema_sjfjuristas.status_proposta (status_proposta_id, nome_status) VALUES
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27', 'Contraproposta Enviada'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', 'Contraproposta Aceita'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', 'Contraproposta Recusada')
ON CONFLICT (nome_status) DO NOTHING;

-- Adiciona o campo de expiração do PIX na tabela de parcelas, caso ainda não exista.
ALTER TABLE schema_sjfjuristas.parcelas_emprestimo
    ADD COLUMN IF NOT EXISTS pix_data_expiracao timestamp(6) with time zone;