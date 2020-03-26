package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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
}
