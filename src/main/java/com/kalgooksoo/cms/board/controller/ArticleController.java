package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.UpdateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.entity.Attachment;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import com.kalgooksoo.cms.board.service.ArticleService;
import com.kalgooksoo.cms.message.CmsMessageSource;
import com.kalgooksoo.core.file.FileIOService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final CmsMessageSource messageSource;

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
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("command.success.create"));

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
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("command.success.update"));

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
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("command.success.delete"));

        // View
        return "redirect:/articles/list?categoryId=" + categoryId;
    }





}
