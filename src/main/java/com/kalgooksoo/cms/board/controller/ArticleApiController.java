package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.UpdateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.service.ArticleService;
import com.kalgooksoo.cms.message.CmsMessageSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

@Tag(name = "ArticleApiController", description = "게시글 API 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final CmsMessageSource messageSource;

    private final ArticleService articleService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Article> post(@Valid CreateArticleCommand command) throws IOException {
        Article article = articleService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    @PostMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> put(
            @PathVariable String id,
            @Valid UpdateArticleCommand command
    ) throws IOException {
        articleService.update(id, command);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @DeleteMapping("/{id}/attachments/{attachmentId}")
    public ResponseEntity<Article> deleteAttachment(
            @PathVariable String id,
            @PathVariable String attachmentId
    ) {
        Article article = articleService.removeAttachment(id, attachmentId);
        return ResponseEntity.ok(article);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<String> handleNoSuchFileException(NoSuchFileException e) {
        logger.error(e.getMessage());
        String message = messageSource.getMessage("error.not.found.resource");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

}
