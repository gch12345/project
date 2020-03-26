package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {
    public void save(Project project) {
        Connection connection = DBUtil.getConnection();
        PreparedStatement statement = null;
        String sql = "insert into project_table values(?,?,?,?,?,?,?)";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, project.getName());
            statement.setString(2, project.getUrl());
            statement.setString(3, project.getDescription());
            statement.setInt(4, project.getStarCount());
            statement.setInt(5, project.getForkCount());
            statement.setInt(6, project.getOpenIssueCount());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            statement.setString(7, simpleDateFormat.format(System.currentTimeMillis()));
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("数据库插入数据出错");
            }
            System.out.println("数据插入成功");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    public List<Project> selectProjectByDate(String date) {
        List<Project> projects = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        String sql = "select name, url, starCount, forkCount, openedIssueCount " +
                "from project_table where date = ? order by starCount desc";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Project project = new Project();
                project.setName(resultSet.getString("name"));
                project.setName(resultSet.getString("url"));
                project.setStarCount(resultSet.getInt("starCount"));
                project.setForkCount(resultSet.getInt("forkCount"));
                project.setOpenIssueCount(resultSet.getInt("openedIssueCount"));
                projects.add(project);
            }
            DBUtil.close(connection, statement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
