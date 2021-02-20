package com.example.jdbcwork;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementDao {

    PreparedStatement ps = null;
    Connection conn = null;
    @Autowired
    private DataSource dataSource;

    public void getConn() throws SQLException {
        conn = dataSource.getConnection();
        conn.setAutoCommit(false);
    }

    public void addMovie(String movieName) throws SQLException {
        try {
            ps = conn.prepareStatement("INSERT INTO movie(name) VALUES (?)");
            ps.setString(1, movieName);

            int result = ps.executeUpdate();

            conn.commit();

            if (result > 0) {
                System.out.println("success");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("wrong in db");
            e.printStackTrace();
            throw e;
        }
    }

    public void queryMovies() {
        try {
            ps = conn.prepareStatement("SELECT * FROM movie");
            var rs = ps.executeQuery();
            while (rs.next()) {
                var movie = Movie.builder().id(rs.getInt("id"))
                        .name(rs.getString("name")).build();
            }
        } catch (SQLException aThrowables) {
            System.out.println("wrong in db");
            aThrowables.printStackTrace();
        }
    }
}
