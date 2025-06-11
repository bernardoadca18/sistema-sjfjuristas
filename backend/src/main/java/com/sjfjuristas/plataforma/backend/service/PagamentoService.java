package com.sjfjuristas.plataforma.backend.service;


import com.sjfjuristas.plataforma.backend.dto.PagamentosParcela.PagamentoParcelaClienteRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PagamentosParcela.PagamentoParcelaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PagamentoService {
    PagamentoParcelaResponseDTO registrarPagamentoCliente(UUID clienteId, PagamentoParcelaClienteRequestDTO request);
    PagamentoParcelaResponseDTO registrarPagamentoAdmin(UUID adminId, Object request);
    List<PagamentoParcelaResponseDTO> getHistoricoPagamentosEmprestimo(UUID emprestimoId);
    List<PagamentoParcelaResponseDTO> getHistoricoPagamentosCliente(UUID clienteId);
}