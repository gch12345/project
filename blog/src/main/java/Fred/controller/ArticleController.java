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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
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

    @RequestMapping(value = "/writer")
    public String writer(HttpSession session, Long activeCid, Model model) {
        User user = (User) session.getAttribute("user");
        List<Article> articles = articleService.queryArticlesByUserId(user.getId());
        model.addAttribute("articleList", articles);
        List<Category> categories = categoryService.queryCategoriesByUserId(user.getId());
        model.addAttribute("categoryList", categories);
        if (activeCid == null) {
            model.addAttribute("activeCid", categories.get(0).getId());
        } else {
            model.addAttribute("activeCid", activeCid);
        }
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

    @RequestMapping(value = "/writer/article/{type}/{id}", method = RequestMethod.POST)
    public String publish(@PathVariable("type") Integer type,
                          @PathVariable("id") Integer id,
                          HttpSession session, Article article, Model model) {
        article.setUpdatedAt(new Date());
        if (type == 1) {
            // 新增文章
            article.setCategoryId(id);
            User user = (User) session.getAttribute("user");
            article.setUserId(user.getId());
            article.setCoverImage("https://picsum.photos/id/1/400/300");
            article.setCreatedAt(new Date());
            article.setStatus((byte)0);
            article.setViewCount(0L);
            article.setCommentCount(0);
            int num = articleService.insert(article);
            id = article.getId().intValue();
        } else {
            // 修改文章
            article.setId(new Long(id));
            int num = articleService.updateByCondition(article);
        }
        return String.format("redirect:/writer/forward/2/%s/editor", id);
    }
}
