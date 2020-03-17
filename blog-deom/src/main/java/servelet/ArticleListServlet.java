package servelet;

import dao.ArticleDAO;
import mouble.Article;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/articleList")
public class ArticleListServlet extends BaseServlet {
    @Override
    public Object process(HttpServletRequest req) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        List<Article> articles = ArticleDAO.listArticle(id);
        return articles;
    }
}
