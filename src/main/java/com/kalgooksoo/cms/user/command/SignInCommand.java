 package com.kalgooksoo.cms.user.command;

 import io.swagger.v3.oas.annotations.Parameter;
 import io.swagger.v3.oas.annotations.media.Schema;
 import jakarta.validation.constraints.NotBlank;
 import jakarta.validation.constraints.NotNull;

 /**
  * 로그인 명령
  *
  * @param username 계정명
  * @param password 패스워드
  */
 @Schema(description = "로그인 명령")
 public record SignInCommand(

         @Parameter(description = "계정명", required = true)
         @Schema(description = "계정명", example = "testuser")
         @NotBlank
         @NotNull
         String username,

         @Parameter(description = "패스워드", required = true)
         @Schema(description = "패스워드", example = "12341234")
         @NotBlank
         @NotNull
         String password

 ) {}