package dao;

import mouble.Article;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
    public static boolean addArticle(Article article) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "insert into article(title, content, user_id, create_time)" +
                    " values (?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getContent());
            ps.setInt(3, article.getUserId());
            ps.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            int num = ps.executeUpdate();
            return num > 0;
        } catch (Exception e) {
            throw new RuntimeException("文章添加jdbc添加失败", e);
        } finally {
            DBUtil.close(connection, ps);
        }
    }

    public static List<Article> listArticle(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select id, title, content, user_id, create_time" +
                    " from article where user_id=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getInt("id"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setUserId(id);
                article.setCreateTime(new java.util.Date(resultSet.getTimestamp("create_time").getTime()));
                articles.add(article);
            }
            return articles;
        } catch (Exception e) {
            throw new RuntimeException("文章添加jdbc添加失败", e);
        } finally {
            DBUtil.close(connection, ps, resultSet);
        }
    }

    public static Article queryArticleById(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select id, title, content, user_id, create_time" +
                    " from article where id=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            Article article = new Article();
            while (resultSet.next()) {
                article.setId(resultSet.getInt("id"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setUserId(id);
                article.setCreateTime(new java.util.Date(resultSet.getTimestamp("create_time").getTime()));
            }
            return article;
        } catch (Exception e) {
            throw new RuntimeException("修改文章失败", e);
        } finally {
            DBUtil.close(connection, ps, resultSet);
        }
    }

    public static boolean updateArticle(Article article) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "update article set title=?, content=? where id=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getContent());
            ps.setInt(3, article.getId());
            int num = ps.executeUpdate();
            return num > 0;
        } catch (Exception e) {
            throw new RuntimeException("文章修改添加失败", e);
        } finally {
            DBUtil.close(connection, ps);
        }
    }
}
