package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.links.entities.Tag;
import com.sivalabs.devzone.links.services.LinkService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Controller("/api/tags")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
@Slf4j
public class TagController {
    private final LinkService linkService;

    @Get
    public List<Tag> allTags() {
        return linkService.findAllTags();
    }
}
