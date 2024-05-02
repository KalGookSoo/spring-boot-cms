package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.UpdateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import com.kalgooksoo.cms.board.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "ArticleController", description = "게시글 컨트롤러")
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
            Model model
    ) {
        // Query

        // Model

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
        model.addAttribute("article", article);

        // View
        return "articles/view";
    }

    @PostMapping
    public String create(
            @ModelAttribute("command") @Valid CreateArticleCommand command,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        // Validation
        if (bindingResult.hasErrors()) {
            return getNew(command, model);
        }

        // Command
        articleService.create(command);

        // Model
        redirectAttributes.addFlashAttribute("message", getMessage("command.success.create", null));

        // View
        return "redirect:/articles/list?categoryId=" + command.categoryId();
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
        return "redirect:/articles/" + id + "/edit";
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
}
