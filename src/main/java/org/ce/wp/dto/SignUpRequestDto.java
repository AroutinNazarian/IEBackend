package org.ce.wp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

/**
 * @author Aroutin Nazarian
 * @since 20.01.23
 */
@Schema(title = "SignUp Request")
public record SignUpRequestDto(@NotBlank String username, @NotBlank String password) {
}
