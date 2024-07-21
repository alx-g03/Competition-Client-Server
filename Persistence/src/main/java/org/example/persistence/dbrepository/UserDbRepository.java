package org.example.persistence.dbrepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.User;
import org.example.persistence.interfaces.UserRepository;
import org.example.persistence.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDbRepository implements UserRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public UserDbRepository(Properties props) {
        logger.info("Initializing UserDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public void add(User elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preSmt = con.prepareStatement("insert into User (username, password) values (?, ?)")) {
            preSmt.setString(1, elem.getUsername());
            preSmt.setString(2, elem.getPassword());
            int result = preSmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(User elem) {

    }

    @Override
    public void update(User elem, Integer id) {

    }

    @Override
    public User findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preSmt = con.prepareStatement("select * from User")) {
            try(ResultSet result = preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(id, username, password);
                    users.add(user);
                }
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return users;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        logger.traceEntry("Find user by username and password: {}, {}", username, password);
        User user = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preSmt = con.prepareStatement("select * from User where username = ? and password = ?")) {
            preSmt.setString(1, username);
            preSmt.setString(2, password);
            try (ResultSet rs = preSmt.executeQuery()) {
                if (rs.next()) {
                    Integer id = rs.getInt("id");
                    String getUsername = rs.getString("username");
                    String getPassword = rs.getString("password");
                    user = new User(id, getUsername, getPassword);
                    logger.trace("Found user: {}", user);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return user;
    }

    @Override
    public Optional<User> getAccountByUsername(String username) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        User user = null;
        try(PreparedStatement statement = con.prepareStatement("select * from User where username=?")){
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Integer id = result.getInt("id");
                    String password = result.getString("password");
                    user = new User(id, username, password);
                }
            }
        } catch(SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(user);
        return Optional.ofNullable(user);
    }
}
