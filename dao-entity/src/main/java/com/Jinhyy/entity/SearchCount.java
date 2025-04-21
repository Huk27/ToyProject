package com.Jinhyy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "search_count", indexes = {
        @Index(name = "idx1_count", columnList = "count"),
        @Index(name = "idx2_keyword", columnList = "keyword")
})
public class SearchCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String keyword;

    @Column(nullable = false)
    private Long count;
}