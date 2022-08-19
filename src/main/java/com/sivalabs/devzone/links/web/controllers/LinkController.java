package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.links.models.LinksDTO;
import com.sivalabs.devzone.links.services.LinkService;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.services.SecurityService;
import com.sivalabs.devzone.users.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;

@ExecuteOn(TaskExecutors.IO)
@Controller("/api")
@RequiredArgsConstructor
@Slf4j
@Secured(SecurityRule.IS_ANONYMOUS)
public class LinkController {
    private final LinkService linkService;
    private final SecurityService securityService;
    private final UserService userService;

    @Get("/links")
    public LinksDTO getLinks(
            @QueryValue(value = "tag", defaultValue = "") String tag,
            @QueryValue(value = "query", defaultValue = "") String query,
            @QueryValue(value = "page", defaultValue = "1") Integer page) {
        if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, page);
            return linkService.searchLinks(query, page);
        } else if (StringUtils.isNotEmpty(tag)) {
            log.info("Fetching links for tag {} with page: {}", tag, page);
            return linkService.getLinksByTag(tag, page);
        } else {
            log.info("Fetching links with page: {}", page);
            return linkService.getAllLinks(page);
        }
    }

    @Get("/links/{id}")
    public LinkDTO getLink(@PathVariable Long id) {
        return linkService
                .getLinkById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Link with id: " + id + " not found"));
    }

    @Post("/links")
    @Status(HttpStatus.CREATED)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Create Link", security = @SecurityRequirement(name = "bearerAuth"))
    public LinkDTO createLink(@Valid @Body LinkDTO link, Authentication authentication) {
        String email = authentication.getName();
        User loginUser = userService.getUserByEmail(email).orElseThrow();
        link.setCreatedUserId(loginUser.getId());
        return linkService.createLink(link);
    }

    @Delete("/links/{id}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Delete Link", security = @SecurityRequirement(name = "bearerAuth"))
    public HttpResponse<Void> deleteLink(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User loginUser = userService.getUserByEmail(email).orElseThrow();
        LinkDTO link = linkService.getLinkById(id).orElse(null);
        this.checkPrivilege(id, link, loginUser);
        linkService.deleteLink(id);
        return HttpResponse.ok();
    }

    private void checkPrivilege(Long linkId, LinkDTO link, User loginUser) {
        if (link == null
                || !(link.getCreatedUserId().equals(loginUser.getId())
                        || securityService.isCurrentUserAdmin())) {
            throw new ResourceNotFoundException("Link not found with id=" + linkId);
        }
    }
}
