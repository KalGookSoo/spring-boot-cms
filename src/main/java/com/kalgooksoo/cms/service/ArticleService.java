package com.kalgooksoo.cms.service;

import com.kalgooksoo.cms.command.CreateArticleCommand;
import com.kalgooksoo.cms.command.UpdateArticleCommand;
import com.kalgooksoo.cms.entity.Article;
import com.kalgooksoo.cms.search.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {

    Page<Article> findAll(@NonNull ArticleSearch search);

    Article create(@NonNull CreateArticleCommand command) throws IOException;

    Article find(@NonNull String id);

    Article update(@NonNull String id, @NonNull UpdateArticleCommand command) throws IOException;

    String delete(@NonNull String id);

    Article addAttachments(@NonNull String id, @NonNull List<MultipartFile> multipartFiles) throws IOException;

    Article removeAttachment(@NonNull String id, @NonNull String attachmentId);

}
