package com.sivalabs.devzone.links.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tags")
@Setter
@Getter
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_generator")
    @SequenceGenerator(name = "tag_id_generator", sequenceName = "tag_id_seq", allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty()
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Link> links;

    @Column(updatable = false, name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(insertable = false, name = "updated_at")
    private Instant updatedAt = Instant.now();

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}
