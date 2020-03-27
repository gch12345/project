package servelet;

import dao.ArticleDAO;
import mouble.Article;
import util.JSONUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
@WebServlet("/articleUpdate")
public class ArticleUpdateServlet extends BaseServlet{
    @Override
    public Object process(HttpServletRequest req) throws Exception {
        Article article = JSONUtil.deserialize(req.getInputStream(), Article.class);
        article.setUserId(1);
        System.out.println("请求数据：" + article);
        if (!ArticleDAO.updateArticle(article)) {
            throw  new RuntimeException("文章修改失败");
        }
        return null;
    }
}
