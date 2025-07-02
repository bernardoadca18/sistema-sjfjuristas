export type LoginRequestDTO = { email: string; senha: string };
export type PreCadastroCheckDTO = { cpf?: string; email?: string; dataNascimento?: string };
export type FinalizarCadastroDTO = { usuarioId: number; senha: string; confirmarSenha: string; aceitouTermosApp: boolean };