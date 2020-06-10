package Fred.controller;

import Fred.model.Article;
import Fred.model.Category;
import Fred.model.Comment;
import Fred.model.User;
import Fred.service.ArticleService;
import Fred.service.CategoryService;
import Fred.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/")
    public String index(Model model) {
        List<Article> articleList = articleService.queryArticles();
        model.addAttribute("articleList", articleList);
        return "index";
    }

    @RequestMapping("/a/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Article article = articleService.queryArticle(id);
        List<Comment> comments = commentService.queryComments(id);
        article.setCommentList(comments);
        model.addAttribute("article", article);
        return "info";
    }

    @RequestMapping("/writer")
    public String writer(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Article> articles = articleService.queryArticlesByUserId(user.getId());
        model.addAttribute("articleList", articles);
        List<Category> categories = categoryService.queryCategoriesByUserId(user.getId());
        model.addAttribute("categoryList", categories);
        model.addAttribute("activeCid", categories.get(0).getId());
        return "writer";
    }

    @RequestMapping("/writer/forward/{type}/{id}/editor")
    public String editor(@PathVariable("type") Long type,
                         @PathVariable("id") Long id,
                         Model model) {
        Category category;
        if (type == 1) {
            category = categoryService.queryCategoryById(id);
            model.addAttribute("activeCid", id);
        } else {
            Article article = articleService.queryArticle(id);
            model.addAttribute("article", article);
            category = categoryService.queryCategoryById((new Long(article.getCategoryId())));
        }
        model.addAttribute("type", type);
        model.addAttribute("category", category);
        return "editor";
    }
}
