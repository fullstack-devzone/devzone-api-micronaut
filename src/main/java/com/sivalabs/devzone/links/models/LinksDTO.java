package com.sivalabs.devzone.links.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.data.model.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinksDTO {
    private List<LinkDTO> data;
    private long totalElements;
    private int pageNumber;
    private int totalPages;

    @JsonProperty("isFirst")
    private boolean isFirst;

    @JsonProperty("isLast")
    private boolean isLast;

    @JsonProperty("hasNext")
    private boolean hasNext;

    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    public LinksDTO(Page<LinkDTO> linksPage) {
        this.setData(linksPage.getContent());
        this.setTotalElements(linksPage.getTotalSize());
        this.setPageNumber(linksPage.getPageNumber() + 1); // 1 - based page numbering
        this.setTotalPages(linksPage.getTotalPages());
        this.setFirst(linksPage.getPageNumber() == 0);
        this.setLast(linksPage.getPageNumber() + 1 == linksPage.getTotalPages());
        this.setHasNext(linksPage.getPageNumber() + 1 < linksPage.getTotalPages());
        this.setHasPrevious(linksPage.getPageNumber() > 0);
    }
}
