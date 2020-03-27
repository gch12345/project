package servelet;

import dao.ArticleDAO;
import mouble.Article;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/articleDetail")
public class ArticleDetailServlet extends BaseServlet{

    @Override
    public Object process(HttpServletRequest req) throws Exception {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Article article = ArticleDAO.queryArticleById(id);
        return article;
    }
}
