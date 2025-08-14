package com.sjfjuristas.plataforma.backend.dto.Admin;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAuthResponseDTO
{
    private UUID adminId;
    private String token;
    private String nomeAdmin;
}
