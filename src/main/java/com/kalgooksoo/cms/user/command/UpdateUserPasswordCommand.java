package com.kalgooksoo.cms.user.command;

import com.kalgooksoo.cms.user.annotation.PasswordsNotEqual;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 계정 패스워드 수정 명령
 */
@Schema(description = "계정 패스워드 수정 명령")
@PasswordsNotEqual
public record UpdateUserPasswordCommand(
        @Parameter(description = "현재 패스워드", required = true)
        @Schema(description = "현재 패스워드", example = "password")
        @NotNull
        @NotBlank
        String originPassword,

        @Parameter(description = "변경할 패스워드", required = true)
        @Schema(description = "변경할 패스워드", example = "newpassword")
        @NotNull
        @NotBlank
        String newPassword
) {}