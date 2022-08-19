package com.sivalabs.devzone.links.repositories;

import com.sivalabs.devzone.links.entities.Link;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query(value = "select id from Link order by createdAt desc",
    countQuery = "select count(l.id) from Link l")
    Page<Long> fetchLinkIds(Pageable pageable);

    @Query(value="select l.id from Link l where lower(l.title) like lower(concat('%', :query,'%')) order by l.createdAt desc",
    countQuery = "select count(l.id) from Link l where lower(l.title) like lower(concat('%', :query,'%'))")
    Page<Long> fetchLinkIdsByTitleContainingIgnoreCase(
            String query, Pageable pageable);

    @Query(value = "select l.id from Link l LEFT JOIN l.tags t where t.name=:tagName order by l.createdAt desc",
    countQuery = "select count(l.id) from Link l LEFT JOIN l.tags t where t.name=:tagName")
    Page<Long> fetchLinkIdsByTag(String tagName, Pageable pageable);

    @Query("select DISTINCT l from Link l left join l. tags left join l.createdBy where l.id in :linkIds")
    List<Link> findLinksWithTags(List<Long> linkIds);

}
