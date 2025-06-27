package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.TipoChavePix;
import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoChavePixRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.TipoChavePixRepository;

@Service
public class TipoChavePixCRUDService
{
    @Autowired
    private TipoChavePixRepository tipoChavePixRepository;

    @Transactional(readOnly = true)
    public Page<TipoChavePixResponseDTO> getAllTiposChavePix(Pageable pageable)
    {
        Page<TipoChavePix> tiposChavePix = tipoChavePixRepository.findAll(pageable);

        return tiposChavePix.map(TipoChavePixResponseDTO::new);
    }
    
    @Transactional(readOnly = true)
    public List<TipoChavePixResponseDTO> getAllTiposChavePix()
    {
        List<TipoChavePix> tiposChavePix = tipoChavePixRepository.findAll();

        return tiposChavePix.stream().map(TipoChavePixResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public TipoChavePixResponseDTO getTipoChavePixById(UUID id)
    {
        TipoChavePix tipoChavePix = tipoChavePixRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        return new TipoChavePixResponseDTO(tipoChavePix);
    }

    @Transactional(readOnly = true)
    public TipoChavePixResponseDTO getTipoChavePixByNomeTipo(String nomeTipo)
    {
        TipoChavePix tipoChavePix = tipoChavePixRepository.findByNomeTipo(nomeTipo).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o nomeTipo informado: " + nomeTipo));

        return new TipoChavePixResponseDTO(tipoChavePix);
    }

    @Transactional
    public TipoChavePixResponseDTO createTipoChavePix(TipoChavePixRequestDTO dto)
    {
        TipoChavePix tipoChavePix = new TipoChavePix();

        tipoChavePix.setNomeTipo(dto.getNomeTipo());
        tipoChavePix = tipoChavePixRepository.save(tipoChavePix);

        return new TipoChavePixResponseDTO(tipoChavePix);
    }

    @Transactional
    public TipoChavePixResponseDTO updateTipoChavePix(UUID id, TipoChavePixRequestDTO dto)
    {
        TipoChavePix tipoChavePix = tipoChavePixRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        tipoChavePix.setNomeTipo(dto.getNomeTipo());
        tipoChavePix = tipoChavePixRepository.save(tipoChavePix);

        return new TipoChavePixResponseDTO(tipoChavePix);
    }

    @Transactional
    public void deleteTipoChavePix(UUID id)
    {
        TipoChavePix tipoChavePix = tipoChavePixRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        tipoChavePixRepository.delete(tipoChavePix);
    }
}