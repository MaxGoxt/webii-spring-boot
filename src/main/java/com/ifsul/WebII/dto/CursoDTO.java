package com.ifsul.WebII.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoDTO(
    @NotBlank String nome,
    @NotBlank String descricao,
    LocalDateTime data_inicio,
    LocalDateTime data_final,
    String img,
    @NotNull Long professor_ID,
    @NotNull Long categoria_ID
) {}

