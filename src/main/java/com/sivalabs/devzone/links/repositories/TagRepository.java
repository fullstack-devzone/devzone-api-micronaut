package com.sivalabs.devzone.links.repositories;

import com.sivalabs.devzone.links.entities.Tag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tag);
}
