package com.kalgooksoo.cms.board.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kalgooksoo.core.page.PageVO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

@Getter
@Setter
@Schema(description = "게시글 검색 조건")
public class ArticleSearch extends PageVO {

    @Parameter(description = "카테고리 식별자")
    @Schema(description = "카테고리 식별자", format = "uuid")
    private final String categoryId;

    @Parameter(description = "제목")
    @Schema(description = "제목", example = "제목")
    private String title;

    @Parameter(description = "본문")
    @Schema(description = "본문", example = "본문")
    private String content;

    public ArticleSearch(String categoryId, String title, String content) {
        Assert.notNull(categoryId, "Category id must not be null");
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
    }

    @JsonIgnore
    public boolean isEmptyTitle() {
        return title == null || title.isEmpty();
    }

    @JsonIgnore
    public boolean isEmptyContent() {
        return content == null || content.isEmpty();
    }

}
