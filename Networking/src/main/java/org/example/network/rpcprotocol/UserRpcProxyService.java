package org.example.network.rpcprotocol;

import org.example.model.Challenge;
import org.example.model.Participant;
import org.example.model.User;
import org.example.network.dtos.ChallengeDto;
import org.example.network.dtos.ParticipationInfoDto;
import org.example.services.Services;
import org.example.services.UserObserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UserRpcProxyService implements Services {
    private String host;
    private int port;

    private UserObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private volatile boolean finished;
    private BlockingQueue<Response> queueResponses;

    public UserRpcProxyService(String host, int port) {
        this.host = host;
        this.port = port;
        queueResponses = new LinkedBlockingQueue<>();
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) {
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error sending object " + e);
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = queueResponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void handleUpdate(Response response) {
        if (response.getType() == ResponseType.UPDATE) {
            client.participationAdded();
        }
    }
    private boolean isUpdateResponse(Response response) {
        return response.getType() == ResponseType.UPDATE;
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Response received " + response);
                    if (response instanceof Response) {
                        Response response1 = (Response) response;
                        if (isUpdateResponse(response1)) {
                            handleUpdate(response1);
                        } else {
                            try {
                                queueResponses.put((Response) response);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    @Override
    public Optional<User> login(String username, String password, UserObserver client) {
        initializeConnection();
        User user = new User(username, password);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            this.client = client;
            return Optional.of((User) response.getData());
        }
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            closeConnection();
            throw new IllegalArgumentException(error);
        }
        return Optional.empty();
    }

    @Override
    public void logout(String username) {
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(username).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new IllegalArgumentException(error);
        }
    }

    @Override
    public List<Challenge> findAllChallenges() {
        Request request = new Request.Builder().type(RequestType.GET_CHALLENGES).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new IllegalArgumentException(error);
        }
        return (List<Challenge>) response.getData();
    }

    @Override
    public List<Participant> findParticipantsByChallengeTypeAndAgeCategory(String type, String ageCategory) {
        var filter = new ChallengeDto(type, ageCategory);
        Request request = new Request.Builder().type(RequestType.GET_PARTICIPANTS_BY_CHALLENGE_TYPE_AGE_CATEGORY).data(filter).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new IllegalArgumentException(error);
        }
        return (List<Participant>) response.getData();
    }

    @Override
    public void addParticipant(String name, Integer age, String type) {
        var addition = new ParticipationInfoDto(name, age, type);
        Request request = new Request.Builder().type(RequestType.ADD_PARTICIPANT).data(addition).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String error = response.getData().toString();
            throw new IllegalArgumentException(error);
        }
    }

    @Override
    public void changeClient(UserObserver userObserver) {
        this.client = userObserver;
    }
}
