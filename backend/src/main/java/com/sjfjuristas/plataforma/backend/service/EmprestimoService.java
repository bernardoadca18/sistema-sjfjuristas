package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoClienteResponseDTO;
//import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoDesembolsoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoAdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;


import java.util.List;
import java.util.UUID;

public interface EmprestimoService {
    EmprestimoClienteResponseDTO getEmprestimoAtualCliente(UUID clienteId);
    EmprestimoAdminResponseDTO getDetalhesEmprestimoAdmin(UUID emprestimoId);
    List<EmprestimoClienteResponseDTO> getHistoricoEmprestimosCliente(UUID clienteId); // Pode precisar de paginação depois
    //void solicitarDesembolso(UUID emprestimoId, EmprestimoDesembolsoRequestDTO request);
    List<ParcelaEmprestimoResponseDTO> getParcelasEmprestimo(UUID emprestimoId);
}
