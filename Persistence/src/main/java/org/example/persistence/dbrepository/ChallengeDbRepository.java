package org.example.persistence.dbrepository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Challenge;
import org.example.persistence.interfaces.ChallengeRepository;
import org.example.persistence.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ChallengeDbRepository implements ChallengeRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ChallengeDbRepository(Properties props) {
        logger.info("Initializing ChallengeDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Iterable<Challenge> findChallengesByType(String type) {
        return null;
    }

    @Override
    public Iterable<Challenge> findChallengesByAgeCategory(String ageCategory) {
        return null;
    }

    @Override
    public Iterable<Challenge> findChallengesByNoParticipants(Integer noParticipants) {
        return null;
    }

    @Override
    public Challenge findChallengeByTypeAndAgeCategory(String type, String ageCategory) {
        logger.traceEntry("type: {}, ageCategory: {}", type, ageCategory);
        Challenge challenge = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preSmt = con.prepareStatement("select * from Challenge where type = ? and age_category = ?")) {
            preSmt.setString(1, type);
            preSmt.setString(2, ageCategory);
            try (ResultSet result = preSmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    int noParticipants = result.getInt("no_participants");
                    challenge = new Challenge(id, type, ageCategory, noParticipants);
                    logger.trace("Found challenge: {}", challenge);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return challenge;
    }


    @Override
    public void add(Challenge elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preSmt = con.prepareStatement("insert into Challenge (type, age_category, no_participants) values (?, ?, ?)")) {
            preSmt.setString(1, elem.getType());
            preSmt.setString(2, elem.getAgeCategory());
            preSmt.setInt(3, elem.getNoParticipants());
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
    public void delete(Challenge elem) {

    }

    @Override
    public void update(Challenge elem, Integer id) {
        logger.traceEntry("updating challenge {}", elem);
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preSmt = con.prepareStatement("update Challenge set type=?, age_category=?, no_participants=? where id=?")) {
            preSmt.setString(1, elem.getType());
            preSmt.setString(2, elem.getAgeCategory());
            preSmt.setInt(3, elem.getNoParticipants());
            preSmt.setInt(4, id);

            int result = preSmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error("Error updating challenge: {}", ex.getMessage(), ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Challenge findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Challenge> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Challenge> challenges = new ArrayList<>();
        try(PreparedStatement preSmt = con.prepareStatement("select * from Challenge")) {
            try(ResultSet result = preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id");
                    String type = result.getString("type");
                    String age_category = result.getString("age_category");
                    int no_participants = result.getInt("no_participants");
                    Challenge challenge = new Challenge(id, type, age_category, no_participants);
                    challenges.add(challenge);
                }
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return challenges;
    }

    @Override
    public Collection<Challenge> getAll() {
        return null;
    }
}
