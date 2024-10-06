package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.UpdateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.entity.Attachment;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import com.kalgooksoo.cms.board.service.ArticleService;
import com.kalgooksoo.core.file.FileIOService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

@Tag(name = "ArticleController", description = "게시글 컨트롤러")
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final MessageSource messageSource;

    @SuppressWarnings("SameParameterValue")
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @GetMapping("/list")
    public String getArticles(
            @ModelAttribute("search") ArticleSearch search,
            Model model
    ) {
        // Query
        Page<Article> page = articleService.findAll(search);

        // Model
        model.addAttribute("page", page);

        // View
        return "articles/list";
    }

    @GetMapping("/new")
    public String getNew(
            @ModelAttribute("command") CreateArticleCommand command,
            BindingResult bindingResult,
            Model model
    ) {
        // Query
        command = bindingResult.hasErrors() ? command : new CreateArticleCommand(command.getCategoryId());

        // Model
        model.addAttribute("command", command);

        // View
        return "articles/new";
    }

    @GetMapping("/{id}")
    public String getView(
            @PathVariable String id,
            Model model
    ) {
        // Query
        Article article = articleService.find(id);

        // Model
        model.addAttribute("category", article.getCategory());
        model.addAttribute("article", article);
        model.addAttribute("attachments", article.getAttachments());

        // View
        return "articles/view";
    }

    @PostMapping
    public String create(
            @ModelAttribute("command") @Valid CreateArticleCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) throws IOException {
        // Validation
        if (bindingResult.hasErrors()) {
            return getNew(command, bindingResult, model);
        }

        // Command
        articleService.create(command);

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.create", null));

        // View
        return "redirect:/articles/list?categoryId=" + command.getCategoryId();
    }

    @GetMapping("/{id}/edit")
    public String getEdit(
            @PathVariable String id,
            @ModelAttribute("command") UpdateArticleCommand command,
            BindingResult bindingResult,
            Model model
    ) {
        // Query
        Article article = articleService.find(id);
        command = bindingResult.hasErrors() ? command : new UpdateArticleCommand(article.getCategory().getId(), article.getTitle(), article.getContent());

        // Model
        model.addAttribute("article", article);
        model.addAttribute("attachments", article.getAttachments());
        model.addAttribute("command", command);

        // View
        return "articles/edit";
    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable String id,
            @ModelAttribute("command") @Valid UpdateArticleCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        // Validation
        if (bindingResult.hasErrors()) {
            return getEdit(id, command, bindingResult, model);
        }

        // Command
        articleService.update(id, command);

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.update", null));

        // View
        return String.format("redirect:/articles/%s/edit", id);
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable String id,
            RedirectAttributes redirectAttributes
    ) {
        // Command
        String categoryId = articleService.delete(id);

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.delete", null));

        // View
        return "redirect:/articles/list?categoryId=" + categoryId;
    }


    @ResponseBody
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

    private String getContentDisposition(String userAgent, String fileName) {
        String disposition = "attachment;filename=";
        if (userAgent.contains("MSIE")) {
            int i = userAgent.indexOf('M', 2);
            String IEV = userAgent.substring(i + 5, i + 8);
            disposition = IEV.equalsIgnoreCase("5.5") ? "filename=" : disposition;
        }
        return disposition + fileName;
    }

    @ResponseBody
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
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }
}
