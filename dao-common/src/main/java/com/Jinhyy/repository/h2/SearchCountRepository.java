package com.Jinhyy.repository.h2;

import com.Jinhyy.entity.SearchCount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchCountRepository extends CrudRepository<SearchCount, Long> {
    SearchCount save(SearchCount searchCount);

    @Query(value = "select * from search_count sc where sc.keyword = :keyword", nativeQuery = true)
    SearchCount findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from search_count sc order by count desc limit 10", nativeQuery = true)
    List<SearchCount> FindTop10OrderByCountDesc();

    @Transactional
    @Modifying
    @Query(value = "UPDATE search_count sc SET sc.count = sc.count + :increased where sc.keyword = :keyword", nativeQuery = true)
    int increaseCount(@Param("keyword") String keyword, @Param("increased") Long increased);
}
