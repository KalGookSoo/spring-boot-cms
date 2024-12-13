package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.entity.Attachment;
import com.kalgooksoo.cms.board.service.ArticleService;
import com.kalgooksoo.cms.message.CmsMessageSource;
import com.kalgooksoo.core.file.FileIOService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Tag(name = "ArticleApiController", description = "게시글 API 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final CmsMessageSource messageSource;

    private final ArticleService articleService;

    @PostMapping(
            value = "/{id}/attachments/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Article> upload(
            @PathVariable String id,
            List<MultipartFile> multipartFiles
    ) throws IOException {
        Article article = articleService.addAttachments(id, multipartFiles);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/{id}/attachments/{attachmentId}/download")
    public void getAttachments(
            @PathVariable String id,
            @PathVariable String attachmentId,
            @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
            HttpServletResponse response
    ) throws IOException {
        Article article = articleService.find(id);
        Attachment attachment = article.getAttachments()
                .stream()
                .filter(e -> attachmentId.equals(e.getId()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        String fileName = URLEncoder.encode(attachment.getOriginalName(), StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = FileIOService.read(attachment.getAbsolutePath());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, getContentDisposition(userAgent, fileName));
        OutputStream outputStream = response.getOutputStream();
        FileCopyUtils.copy(inputStream, outputStream);
    }

    @GetMapping("/{id}/attachments/download-zip")
    public void getAttachments(
            @PathVariable String id,
            @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
            HttpServletResponse response
    ) throws IOException {
        Article article = articleService.find(id);
        Set<Attachment> attachments = article.getAttachments();
        if (attachments.isEmpty()) {
            throw new NoSuchElementException();
        }
        String fileName = URLEncoder.encode(article.getTitle(), StandardCharsets.UTF_8);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, getContentDisposition(userAgent, fileName) + ".zip");

        try (ZipOutputStream outputStream = new ZipOutputStream(response.getOutputStream())) {
            for (Attachment attachment : attachments) {
                ZipEntry entry = new ZipEntry(attachment.getOriginalName());
                outputStream.putNextEntry(entry);
                ByteArrayInputStream inputStream = FileIOService.read(attachment.getAbsolutePath());
                StreamUtils.copy(inputStream, outputStream);
                outputStream.closeEntry();
            }
        }

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

    private String getContentDisposition(String userAgent, String fileName) {
        String disposition = "attachment;filename=";
        if (userAgent.contains("MSIE")) {
            int i = userAgent.indexOf('M', 2);
            String IEV = userAgent.substring(i + 5, i + 8);
            disposition = IEV.equalsIgnoreCase("5.5") ? "filename=" : disposition;
        }
        return disposition + fileName;
    }

}
