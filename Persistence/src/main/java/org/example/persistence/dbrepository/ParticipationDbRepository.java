package org.example.persistence.dbrepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Participation;
import org.example.persistence.interfaces.ParticipationRepository;
import org.example.persistence.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ParticipationDbRepository implements ParticipationRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ParticipationDbRepository(Properties props) {
        logger.info("Initializing ParticipationDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Iterable<Participation> findParticipationsByParticipantId(Integer participantId) {
        return null;
    }

    @Override
    public Iterable<Participation> findParticipationsByChallengeId(Integer challengeId) {
        logger.traceEntry("challengeId: {}", challengeId);
        Connection con = dbUtils.getConnection();
        List<Participation> participations = new ArrayList<>();
        try (PreparedStatement preSmt = con.prepareStatement("select * from Participation where challenge_id = ?")) {
            preSmt.setInt(1, challengeId);
            try (ResultSet result = preSmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int participantId = result.getInt("participant_id");
                    Participation participation = new Participation(id, participantId, challengeId);
                    participations.add(participation);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return participations;
    }

    @Override
    public void add(Participation elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preSmt = con.prepareStatement("insert into Participation (participant_id, challenge_id) values (?, ?)")) {
            preSmt.setInt(1, elem.getParticipantId());
            preSmt.setInt(2, elem.getChallengeId());
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
    public void delete(Participation elem) {

    }

    @Override
    public void update(Participation elem, Integer id) {

    }

    @Override
    public Participation findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Participation> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participation> participations = new ArrayList<>();
        try(PreparedStatement preSmt = con.prepareStatement("select * from Participation")) {
            try(ResultSet result = preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id");
                    int participant_id = result.getInt("participant_id");
                    int challenge_id = result.getInt("challenge_id");
                    Participation participation = new Participation(id, participant_id, challenge_id);
                    participations.add(participation);
                }
            }
        }
        catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error db" + ex);
        }
        logger.traceExit();
        return participations;
    }

    @Override
    public Collection<Participation> getAll() {
        return null;
    }
}
