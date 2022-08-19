package com.sivalabs.devzone.links.web.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
@Slf4j
public class PageMetadataController {

    @Get("/api/page-metadata")
    public Map<String, String> getPageMetadata(@QueryValue String url) {
        Map<String, String> metadata = new ConcurrentHashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            metadata.put("title", doc.title());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            metadata.put("title", url);
        }
        return metadata;
    }
}
