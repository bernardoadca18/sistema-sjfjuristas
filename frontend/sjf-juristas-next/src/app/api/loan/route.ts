import { NextResponse } from 'next/server';

export async function POST(request: Request) {
  try {
    const formData = await request.formData();
    
    const loanValue = formData.get('loanValue');
    const fullName = formData.get('fullName');
    const cpf = formData.get('cpf');
    const email = formData.get('email');
    const whatsapp = formData.get('whatsapp');
    const termsAccepted = formData.get('termsAccepted');
    
    // Arquivos
    const rgCnhFront = formData.get('rgCnhFront') as File | null;
    const rgCnhBack = formData.get('rgCnhBack') as File | null;
    const proofOfAddress = formData.get('proofOfAddress') as File | null;

    // Log para fins de depuração
    console.log('Solicitação Recebida:');
    console.log({
        loanValue,
        fullName,
        cpf,
        email,
        whatsapp,
        termsAccepted
    });
    console.log('Arquivos:', {
        rgCnhFront: rgCnhFront ? { name: rgCnhFront.name, size: rgCnhFront.size } : 'N/A',
        rgCnhBack: rgCnhBack ? { name: rgCnhBack.name, size: rgCnhBack.size } : 'N/A',
        proofOfAddress: proofOfAddress ? { name: proofOfAddress.name, size: proofOfAddress.size } : 'N/A',
    });

    // ===================================================================
    // TODO: IMPLEMENTAR LÓGICA DE NEGÓCIO AQUI
    // 1. Salvar os dados em um banco de dados (ex: PostgreSQL, MongoDB).
    // 2. Fazer upload dos arquivos para um serviço de storage (ex: AWS S3, Google Cloud Storage).
    // 3. Enviar um e-mail de notificação para a equipe interna.
    // 4. Enviar um e-mail de confirmação para o cliente.
    // ===================================================================

    return NextResponse.json({ success: true, message: 'Solicitação recebida com sucesso!' });

  } catch (error) {
    console.error('Erro na API de solicitação:', error);
    return NextResponse.json({ success: false, message: 'Erro interno do servidor.' }, { status: 500 });
  }
}