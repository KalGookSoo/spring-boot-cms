package com.kalgooksoo.cms;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsoupTest {

    @DisplayName("스크립트 태그 제거")
    @Test
    void xssProtectTestCase1() {
        // Given
        String html = """
                <h1>제목</h1>
                <p>본문</p>
                <script>alert('이게 실행되면 안된다고')</script>
                """;

        // When
        String cleanedHtml = Jsoup.clean(html, Safelist.relaxed());

        // Then
        System.out.println(cleanedHtml + "\n\n\n");
        assertFalse(cleanedHtml.contains("<script>"));
        assertFalse(cleanedHtml.contains("이게 실행되면 안된다고"));
    }
    
    @DisplayName("인라인 스크립트 제거")
    @Test
    void xssProtectTestCase2() {
        // Given
        String html = """
                <a href="javascript:alert('이게 실행되면 안된다고')">매우 안전한 링크</a>
                <a href="https://example.com">안전한 링크</a>
                """;
    
        // When
        String cleanedHtml = Jsoup.clean(html, Safelist.relaxed());

        // Then
        System.out.println(cleanedHtml + "\n\n\n");
        assertFalse(cleanedHtml.contains("javascript:"));
        assertTrue(cleanedHtml.contains("https://example.com"));
    }

    @DisplayName("태그 오탈자 보정")
    @Test
    void typoCorrectionTest() {
        // Given
        String htmlWithErrors = """
                <h1>오탈자</h2>
                <p>오탈자</ul>
                """;
    
        // When
        String cleanedHtml = Jsoup.clean(htmlWithErrors, Safelist.relaxed());
    
        // Then
        System.out.println(cleanedHtml + "\n\n\n");
        assertTrue(cleanedHtml.contains("<h1>오탈자</h1>"));
        assertTrue(cleanedHtml.contains("<p>오탈자</p>"));
    }

    @DisplayName("XSS 차단 및 태그 이스케이프")
    @Test
    void protectAndEscapeTest() {
        // Given
        String html = """
                <h1>제목</h1>
                <p>본문</p>
                <script>alert('이게 실행되면 안된다고')</script>
                """;

        // When
        String cleanedHtml = Jsoup.clean(html, Safelist.relaxed());
        String escapedHtml = Entities.escape(cleanedHtml);

        // Then
        System.out.println(escapedHtml + "\n\n\n");
        assertTrue(escapedHtml.contains("&lt;h1&gt;제목&lt;/h1&gt;"));
        assertTrue(escapedHtml.contains("&lt;p&gt;본문&lt;/p&gt;"));
    }

}