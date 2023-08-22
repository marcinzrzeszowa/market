package com.mj.market.app.article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository  extends JpaRepository<Article,Long> {

    List<Article> findAll();

    Optional<Article> findById(Long id);

    Article save(Article entity);

    boolean existsById(Long id);

    void deleteById(Long id);
}
