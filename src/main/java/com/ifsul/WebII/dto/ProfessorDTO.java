package com.ifsul.WebII.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfessorDTO(
		@NotBlank String nome, 
		@NotBlank String email
		) {

}
