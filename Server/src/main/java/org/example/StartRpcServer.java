package org.example;

import org.example.network.utils.AbstractServer;
import org.example.network.utils.RpcConcurrentServer;
import org.example.persistence.dbrepository.ChallengeDbRepository;
import org.example.persistence.dbrepository.ParticipantDbRepository;
import org.example.persistence.dbrepository.ParticipationDbRepository;
import org.example.persistence.dbrepository.UserDbRepository;
import org.example.persistence.hibernaterepository.UserHibernateRepository;
import org.example.persistence.interfaces.ChallengeRepository;
import org.example.persistence.interfaces.ParticipantRepository;
import org.example.persistence.interfaces.ParticipationRepository;
import org.example.persistence.interfaces.UserRepository;
import org.example.server.ServicesImpl;
import org.example.services.Services;

import java.io.FileReader;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("Server/bd.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot find bd.properties " + e);
        }
        Properties propertiesServer = new Properties();
        try {
            propertiesServer.load(new FileReader("Server/server.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot find server.properties " + e);
        }
        // UserRepository userRepository = new UserDbRepository(properties);
        UserRepository userRepository = new UserHibernateRepository();
        ChallengeRepository challengeRepository = new ChallengeDbRepository(properties);
        ParticipantRepository participantRepository = new ParticipantDbRepository(properties);
        ParticipationRepository participationRepository = new ParticipationDbRepository(properties);
        Services services = new ServicesImpl(userRepository, challengeRepository, participantRepository, participationRepository);
        System.out.println("Starting server on port " + propertiesServer.getProperty("port"));
        var portInt = Integer.parseInt(propertiesServer.getProperty("port"));
        AbstractServer server = new RpcConcurrentServer(portInt, services);
        try{
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server " + e.getMessage());
        }
    }
}
