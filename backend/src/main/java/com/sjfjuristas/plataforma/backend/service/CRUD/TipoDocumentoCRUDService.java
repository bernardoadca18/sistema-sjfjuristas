package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.TipoDocumento;
import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoDocumentoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoDocumentoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.TipoDocumentoRepository;

@Service
public class TipoDocumentoCRUDService
{
    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;


    @Transactional(readOnly = true)
    public Page<TipoDocumentoResponseDTO> getAllTiposDocumento(Pageable pageable)
    {
        Page<TipoDocumento> tiposDocumento = tipoDocumentoRepository.findAll(pageable);

        return tiposDocumento.map(TipoDocumentoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<TipoDocumentoResponseDTO> getAllTiposDocumento()
    {
        List<TipoDocumento> tiposDocumento = tipoDocumentoRepository.findAll();

        return tiposDocumento.stream().map(TipoDocumentoResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public TipoDocumentoResponseDTO getTipoDocumentoById(UUID id)
    {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        return new TipoDocumentoResponseDTO(tipoDocumento);
    }

    @Transactional(readOnly = true)
    public TipoDocumentoResponseDTO getTipoDocumentoByTipoDocumento(String nomeDocumento)
    {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByNomeDocumento(nomeDocumento).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o nomeDocumento informado: " + nomeDocumento));

        return new TipoDocumentoResponseDTO(tipoDocumento);
    }

    @Transactional 
    public TipoDocumentoResponseDTO createTipoDocumento(TipoDocumentoRequestDTO dto)
    {
        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNomeDocumento(dto.getNomeDocumento());
        tipoDocumento.setDescricao(dto.getDescricao());
        tipoDocumento.setObrigatorioProposta(dto.isObrigatorioProposta());
        
        tipoDocumento = tipoDocumentoRepository.save(tipoDocumento);

        return new TipoDocumentoResponseDTO(tipoDocumento);
    }

    @Transactional 
    public TipoDocumentoResponseDTO updateTipoDocumento(UUID id, TipoDocumentoRequestDTO dto)
    {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        tipoDocumento.setNomeDocumento(dto.getNomeDocumento());
        tipoDocumento.setDescricao(dto.getDescricao());
        tipoDocumento.setObrigatorioProposta(dto.isObrigatorioProposta());
        
        tipoDocumento = tipoDocumentoRepository.save(tipoDocumento);

        return new TipoDocumentoResponseDTO(tipoDocumento);
    }

    @Transactional 
    public void deleteTipoDocumento(UUID id)
    {
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));
        
        tipoDocumentoRepository.delete(tipoDocumento);
    }
}

