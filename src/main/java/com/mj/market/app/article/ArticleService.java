package com.mj.market.app.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createArticle(Article article) {
        Article newArticle = new Article();
        newArticle.setShortDescription(article.getShortDescription());
        newArticle.setDescription(article.getShortDescription());
        newArticle.setLocalDate(article.getLocalDate());
        articleRepository.save(newArticle);
        return newArticle;
    }

    //throws ResourceNotFoundException
    public Optional<Article> updateArticle(Long id, Article changedArticle) throws Exception{
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()){
            throw new Exception();
        }
        else{
            article.get().setShortDescription(changedArticle.getShortDescription());
            article.get().setDescription(changedArticle.getShortDescription());
            article.get().setLocalDate(changedArticle.getLocalDate());
            articleRepository.save(article.get());
            return article;
        }
    }

    public Article getArticle(Long id){
        return articleRepository.findById(id).orElseThrow(()->new RuntimeException("Artykuł nie istnieje"));
    }

    public List<Article> getAllArticles( ){
        return articleRepository.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Article saveArticle(Article article) {
        if(!article.getDescription().isEmpty() && !article.getShortDescription().isEmpty()){
            articleRepository.save(article);
        }
        return article;
    }

    public void deleteArticle(Long id) {
    if (!articleRepository.existsById(id)){
        throw new IllegalStateException("Nie możesz usunąć nie istniejącego artykułu");
    }

    articleRepository.deleteById(id);
    }

    public Article updateOrSaveArticle(Long id, Article newArticle) {
            Article article = articleRepository.findById(id)
                    .map(element ->{
                        element.setShortDescription(newArticle.getShortDescription());
                        element.setDescription(newArticle.getDescription());
                        element.setLocalDate(newArticle.getLocalDate());
                        return  articleRepository.save(element);
                    }).orElseGet(()->{
                        return articleRepository.save(newArticle);
                    });
            return article;
    }

    public Article findArticle(Long articleId) {
       // return articleRepository.findById(articleId).stream().findFirst().orElse(null);
        return articleRepository.findById(articleId).get();
    }

    public void saveArticles(List<Article> list) {
        articleRepository.saveAll(list);
    }
}
