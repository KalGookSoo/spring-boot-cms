package com.kalgooksoo.cms.board.command;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "게시글 생성 커맨드")
public record CreateArticleCommand(
        @Parameter(description = "카테고리 식별자", required = true)
        @Schema(description = "카테고리 식별자", example = "카테고리 식별자")
        @NotBlank
        @NotNull
        String categoryId,

        @Parameter(description = "제목", required = true)
        @Schema(description = "제목", example = "제목")
        @NotBlank
        @NotNull
        String title,

        @Parameter(description = "본문", required = true)
        @Schema(description = "본문", example = "본문")
        @NotBlank
        @NotNull
        String content
) {
}
