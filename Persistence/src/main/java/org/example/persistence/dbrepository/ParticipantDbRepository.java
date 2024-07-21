package org.example.persistence.dbrepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Participant;
import org.example.persistence.interfaces.ParticipantRepository;
import org.example.persistence.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ParticipantDbRepository implements ParticipantRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ParticipantDbRepository(Properties props) {
        logger.info("Initializing ParticipantDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Participant findParticipantByName(String name) {
        logger.traceEntry("name: {}", name);
        Connection con = dbUtils.getConnection();
        Participant participant = null;
        try (PreparedStatement preSmt = con.prepareStatement("select * from Participant where name = ?")) {
            preSmt.setString(1, name);
            try (ResultSet result = preSmt.executeQuery()) {
                if (result.next()) {
                    Integer id = result.getInt("id");
                    Integer age = result.getInt("age");
                    int no_challenges = result.getInt("no_challenges");
                    participant = new Participant(id, name, age, no_challenges);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return participant;
    }

    @Override
    public Iterable<Participant> findParticipantsByAge(Integer age) {
        return null;
    }

    @Override
    public Iterable<Participant> findParticipantsByNoChallenges(Integer noChallenges) {
        return null;
    }

    @Override
    public void add(Participant elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preSmt = con.prepareStatement("insert into Participant (name, age, no_challenges) values (?, ?, ?)")) {
            preSmt.setString(1, elem.getName());
            preSmt.setInt(2, elem.getAge());
            preSmt.setInt(3, elem.getNoChallenges());
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
    public void delete(Participant elem) {

    }

    @Override
    public void update(Participant elem, Integer id) {
        logger.traceEntry("updating participant {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preSmt = con.prepareStatement("update Participant set name=?, age=?, no_challenges=? where id=?")) {
            preSmt.setString(1, elem.getName());
            preSmt.setInt(2, elem.getAge());
            preSmt.setInt(3, elem.getNoChallenges());
            preSmt.setInt(4, id);

            int result = preSmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Participant findById(Integer id) {
        logger.traceEntry("id: {}", id);
        Connection con = dbUtils.getConnection();
        Participant participant = null;
        try (PreparedStatement preSmt = con.prepareStatement("select * from Participant where id = ?")) {
            preSmt.setInt(1, id);
            try (ResultSet result = preSmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    Integer age = result.getInt("age");
                    int no_challenges = result.getInt("no_challenges");
                    participant = new Participant(id, name, age, no_challenges);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return participant;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try(PreparedStatement preSmt = con.prepareStatement("select * from Participant")) {
            try(ResultSet result = preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    Integer age = result.getInt("age");
                    int no_challenges = result.getInt("no_challenges");
                    Participant participant = new Participant(id, name, age, no_challenges);
                    participants.add(participant);
                }
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return participants;
    }

    @Override
    public Collection<Participant> getAll() {
        return null;
    }
}
