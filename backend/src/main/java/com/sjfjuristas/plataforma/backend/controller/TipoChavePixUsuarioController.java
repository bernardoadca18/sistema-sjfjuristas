package com.sjfjuristas.plataforma.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.TipoChavePixCRUDService;

@RestController
@RequestMapping("/api/tipo-chave-pix")
public class TipoChavePixUsuarioController
{
    @Autowired
    private TipoChavePixCRUDService tipoChavePixCRUDService;

    @GetMapping ("/non-paged")
    public ResponseEntity<List<TipoChavePixResponseDTO>> getAllTiposChavePixNonPaged()
    {
        List<TipoChavePixResponseDTO> response = tipoChavePixCRUDService.getAllTiposChavePix();
        return ResponseEntity.ok(response);
    }

    @GetMapping 
    public ResponseEntity<Page<TipoChavePixResponseDTO>> getAllTiposChavePix(@PageableDefault(page = 0, size = 10, sort = "nomeTipo") Pageable pageable)
    {
        Page<TipoChavePixResponseDTO> response = tipoChavePixCRUDService.getAllTiposChavePix(pageable);
        return ResponseEntity.ok(response);
    }
}
