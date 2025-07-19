package com.sjfjuristas.plataforma.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CadastroNaoAprovadoException extends RuntimeException
{

    public CadastroNaoAprovadoException(String message)
    {
        super(message);
    }
}