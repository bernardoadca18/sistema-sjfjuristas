-- =================================================================
-- INSERÇÃO DE DADOS BÁSICOS (SEED DATA)
-- =================================================================
ALTER TABLE schema_sjfjuristas.perfis_usuario ALTER COLUMN perfil_id SET DEFAULT gen_random_uuid();
ALTER TABLE schema_sjfjuristas.tipos_chave_pix ALTER COLUMN tipo_chave_pix_id SET DEFAULT gen_random_uuid();
ALTER TABLE schema_sjfjuristas.tipos_documento ALTER COLUMN tipo_documento_id SET DEFAULT gen_random_uuid();
ALTER TABLE schema_sjfjuristas.status_proposta ALTER COLUMN status_proposta_id SET DEFAULT gen_random_uuid();
ALTER TABLE schema_sjfjuristas.status_pagamento_parcela ALTER COLUMN status_pagamento_parcela_id SET DEFAULT gen_random_uuid();
ALTER TABLE schema_sjfjuristas.status_emprestimo ALTER COLUMN status_emprestimo_id SET DEFAULT gen_random_uuid();
ALTER TABLE schema_sjfjuristas.ocupacoes ALTER COLUMN ocupacao_id SET DEFAULT gen_random_uuid();
ALTER TABLE schema_sjfjuristas.administradores ALTER COLUMN adminstrador_id SET DEFAULT gen_random_uuid();

-- Inserção de dados na tabela: perfis_usuario
INSERT INTO "schema_sjfjuristas"."perfis_usuario" (nome_perfil) VALUES
('Cliente'),
('Administrador')
ON CONFLICT (nome_perfil) DO NOTHING;

-- Inserção de dados na tabela: tipos_chave_pix
INSERT INTO "schema_sjfjuristas"."tipos_chave_pix" (nome_tipo) VALUES
('CPF'),
('CNPJ'),
('E-mail'),
('Telefone'),
('Chave Aleatória')
ON CONFLICT (nome_tipo) DO NOTHING;

-- Inserção de dados na tabela: tipos_documento
INSERT INTO "schema_sjfjuristas"."tipos_documento" (obrigatorio_proposta, nome_documento, descricao) VALUES
(true, 'RG/CNH (Frente)', 'Foto da parte frontal do documento de identidade com foto (RG ou CNH).'),
(true, 'RG/CNH (Verso)', 'Foto da parte de trás do documento de identidade com foto (RG ou CNH).'),
(true, 'Comprovante de Residência', 'Conta de consumo recente (água, luz, telefone) ou outro documento válido como comprovante de endereço.'),
(true, 'Comprovante de Renda', 'Holerite, declaração de imposto de renda, extrato bancário ou outro documento que comprove os rendimentos.'),
(false, 'Selfie de Validação', 'Uma selfie do usuário segurando o documento de identidade ao lado do rosto.')
ON CONFLICT (nome_documento) DO NOTHING;

-- Inserção de dados na tabela: status_proposta
INSERT INTO "schema_sjfjuristas"."status_proposta" (nome_status) VALUES
('Pendente de Análise'),
('Documentação Pendente'),
('Em Análise'),
('Aprovada'),
('Negada'),
('Finalizada'),
('Contraproposta Enviada'),
('Contraproposta Aceita'),
('Contraproposta Recusada')
ON CONFLICT (nome_status) DO NOTHING;

-- Inserção de dados na tabela: status_pagamento_parcela
INSERT INTO "schema_sjfjuristas"."status_pagamento_parcela" (nome_status) VALUES
('Pendente'),
('Pago'),
('Atrasado'),
('Pago com Atraso')
ON CONFLICT (nome_status) DO NOTHING;

-- Inserção de dados na tabela: status_emprestimo
INSERT INTO "schema_sjfjuristas"."status_emprestimo" (nome_status) VALUES
('Pendente Desembolso'),
('Ativo'),
('Finalizado'),
('Inadimplente'),
('Cancelado'),
('Em Análise')
ON CONFLICT (nome_status) DO NOTHING;

INSERT INTO schema_sjfjuristas.ocupacoes (nome_ocupacao) VALUES
('Advogado(a)'),
('Agrônomo(a)'),
('Arquiteto(a)'),
('Artista'),
('Assistente Administrativo'),
('Assistente Social'),
('Atendente de Telemarketing'),
('Autônomo(a) / Profissional Liberal'),
('Bombeiro(a)'),
('Bancário(a)'),
('Cabeleireiro(a)'),
('Cientista de Dados'),
('Contador(a)'),
('Cozinheiro(a) / Chef'),
('Dentista'),
('Designer Gráfico'),
('Desenvolvedor(a) de Software'),
('Eletricista'),
('Empresário(a)'),
('Enfermeiro(a)'),
('Engenheiro(a) Civil'),
('Engenheiro(a) de Produção'),
('Engenheiro(a) Elétrico'),
('Engenheiro(a) Mecânico'),
('Estudante'),
('Farmacêutico(a)'),
('Fisioterapeuta'),
('Fotógrafo(a)'),
('Funcionário(a) Público(a)'),
('Garçom / Garçonete'),
('Gerente de Projetos'),
('Jornalista'),
('Marceneiro(a)'),
('Mecânico(a) de Automóveis'),
('Médico(a)'),
('Militar'),
('Motorista'),
('Músico(a)'),
('Nutricionista'),
('Operador(a) de Caixa'),
('Pedreiro(a)'),
('Policial'),
('Professor(a)'),
('Psicólogo(a)'),
('Publicitário(a)'),
('Recepcionista'),
('Representante Comercial'),
('Técnico(a) em Informática'),
('Técnico(a) em Enfermagem'),
('Vendedor(a)'),
('Veterinário(a)'),
('Zelador(a)'),
('Analista de Marketing'),
('Analista Financeiro'),
('Artesão(ã)'),
('Atleta'),
('Aviador(a) / Piloto(a)'),
('Biólogo(a)'),
('Biomédico(a)'),
('Consultor(a)'),
('Corretor(a) de Imóveis'),
('Cientista'),
('Cuidador(a) de Idosos'),
('Diarista / Empregado(a) Doméstico(a)'),
('Economista'),
('Editor(a) de Vídeo'),
('Geólogo(a)'),
('Gestor(a) de Recursos Humanos'),
('Historiador(a)'),
('Investidor(a)'),
('Manobrista'),
('Maquiador(a)'),
('Marinheiro(a)'),
('Modelo'),
('Músico Terapeuta'),
('Padeiro(a) / Confeiteiro(a)'),
('Porteiro(a)'),
('Produtor(a) de Eventos'),
('Químico(a)'),
('Radiologista'),
('Redator(a) / Revisor(a)'),
('Segurança / Vigilante'),
('Sociólogo(a)'),
('Soldador(a)'),
('Tradutor(a) / Intérprete'),
('Turismólogo(a)'),
('Urbanista'),
('Web Designer'),
('Outros')
ON CONFLICT (nome_ocupacao) DO NOTHING;

INSERT INTO schema_sjfjuristas.administradores(nome_completo, username, email, hash_senha, telefone_contato, data_cadastro, ultimo_login, ativo, email_verificado, perfil_id_perfis_usuario, cargo_interno, departamento, matricula_funcionario)
VALUES ('Bernardo Alves Aguiar da Cunha', 'bernardoalvesaguiar6', 'bernardoalvesaguiar16@gmail.com', '$2a$10$Ps0uEbYP.T6h9XTpWKXym.jE3M79kN94TqxNs/pQP9q8SIbyV7//K', '31988620039', '2025-08-13', '2025-08-13', true, true, '18b4037b-72c3-4e47-87c2-93123e002c79', 'Desenvolvedor do Sistema', 'DEV', 'bernardoalvesaguiar6');
