package com.sjfjuristas.plataforma.backend.service;


import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import java.util.List;
import java.util.UUID;

public interface ParcelaEmprestimoService {
    List<ParcelaEmprestimoResponseDTO> getParcelasPorEmprestimo(UUID emprestimoId);
    ParcelaEmprestimoResponseDTO getDetalhesParcela(UUID parcelaId);
}