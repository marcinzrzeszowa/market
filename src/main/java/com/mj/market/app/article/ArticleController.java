package com.mj.market.app.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = {"/","/index","/home","/articles"})
    public String showIndex(Model model){
        model.addAttribute("articles", articleService.getAllArticles());
        return "home";
    }

    @GetMapping("/articles/article/{id}")
    public String editProduct(@PathVariable("id") Long articleId,
                              Model model){
        Article article = articleService.findArticle(articleId);
        if (article != null){
            model.addAttribute("article", article);
            return "article_details";
        }else {
            return "error/404";
        }
    }

    @GetMapping("/articles/new")
    public String newArticle(Model model) {
        model.addAttribute("articleForm", new Article());
        model.addAttribute("action", "newArticle");
        return "article";
    }

    @PostMapping("/articles/new")
    public String newArticle(@ModelAttribute("articleForm") Article article,
                             BindingResult bindingResult,
                             Model model) {
        if(bindingResult.hasErrors()){
            logger.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("action","newArticle");
            return "article";
        }
        articleService.saveArticle(article);
        logger.debug(String.format("Dodoano artykuł id: %s", article.getId()));
        return "redirect:/home";
    }
    
    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable("id") Long articleId,
                              Model model){
        Article article = articleService.findArticle(articleId);
        if (article != null){
            model.addAttribute("articleForm", article);
            model.addAttribute("action", "editArticle");
            return "article";
        }else {
            return "error/404";
        }
    }

    @PostMapping("/articles/edit/{id}")
    public String updateArticle( @PathVariable("id") Long id,
                                 @ModelAttribute("articleForm") Article article,
                                 BindingResult bindingResult,
                                 Model model) {
        if(bindingResult.hasErrors()){
        model.addAttribute("action","editArticle");
        return "article";
        }
        articleService.updateOrSaveArticle(id,article);
        return "redirect:/home";
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id){
        Article article = articleService.findArticle(id);
        if(article!=null){
            articleService.deleteArticle(id);
            return "redirect:/home";
        } else {
            return "error/404";
        }
    }
}
