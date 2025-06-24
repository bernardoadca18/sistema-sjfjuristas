package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.Ocupacao;
import com.sjfjuristas.plataforma.backend.repository.OcupacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OcupacaoService
{
    @Autowired
    private OcupacaoRepository ocupacaoRepository;

    public List<Ocupacao> listarOcupacoesAtivas()
    {
        return ocupacaoRepository.findByAtivoTrueOrderByNomeOcupacaoAsc();
    }
}