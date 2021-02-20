package com.example.jdbcwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addMovie(String movieName) {
        jdbcTemplate.update("INSERT INTO movie(name) VALUES(?)", movieName);
    }

    public void deleteMovie(String movieName) {
        jdbcTemplate.update("DELETE FROM movie WHERE name=?", movieName);
    }

    public void changeMovieName(String aOldMovieName, String aNewMovieName) {
        jdbcTemplate.update("UPDATE move SET name=? WHERE name=?", aNewMovieName, aOldMovieName);
    }

    public void queryMovies() {
        List<Movie> movieList = jdbcTemplate.query("SELECT * FROM movie",
                new RowMapper<Movie>() {
                    @Override
                    public Movie mapRow(ResultSet aResultSet, int aI) throws SQLException {
                        return Movie.builder()
                                .id(aResultSet.getInt(1))
                                .name(aResultSet.getString(2))
                                .build();
                    }
                });

        movieList.forEach(m -> System.out.println(m.getId() + ": " + m.getName()));
    }
}
