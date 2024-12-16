package com.kalgooksoo.cms.board.controller;

import com.kalgooksoo.cms.board.command.CreateArticleCommand;
import com.kalgooksoo.cms.board.command.UpdateArticleCommand;
import com.kalgooksoo.cms.board.entity.Article;
import com.kalgooksoo.cms.board.search.ArticleSearch;
import com.kalgooksoo.cms.board.service.ArticleService;
import com.kalgooksoo.cms.message.CmsMessageSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable String id,
            RedirectAttributes redirectAttributes
    ) {
        // Command
        String categoryId = articleService.delete(id);

        // Model
        String message = messageSource.getMessage("command.success.delete");
        redirectAttributes.addFlashAttribute("message", message);

        // View
        return "redirect:/articles/list?categoryId=" + categoryId;
    }





}
