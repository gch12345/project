package servelet;

import dao.ArticleDAO;
import mouble.Article;
import util.JSONUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebServlet("/articleAdd")
public class ArticleAddServlet extends BaseServlet {

    @Override
    public Object process(HttpServletRequest req) throws IOException {
        Article article = JSONUtil.deserialize(req.getInputStream(), Article.class);
        article.setUserId(1);
        System.out.println("请求数据：" + article);
        if (!ArticleDAO.addArticle(article)) {
            throw  new RuntimeException("文章添加失败");
        }
        return null;
    }
}
