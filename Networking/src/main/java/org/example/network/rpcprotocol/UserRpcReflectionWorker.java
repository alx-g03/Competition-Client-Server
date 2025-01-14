package org.example.network.rpcprotocol;

import org.example.model.User;
import org.example.network.dtos.ChallengeDto;
import org.example.network.dtos.ParticipationInfoDto;
import org.example.services.Services;
import org.example.services.UserObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserRpcReflectionWorker implements Runnable, UserObserver {
    private Services server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public UserRpcReflectionWorker(Services server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (
                IOException e) {
            System.out.println("Error " + e);
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.getType() == RequestType.LOGIN) {
            System.out.println("Login request ...");
            User user = (User) request.getData();
            try {
                var optional = server.login(user.getUsername(), user.getPassword(), this);
                if (optional.isPresent()) {
                    return new Response.Builder().type(ResponseType.OK).data(user).build();
                }
                else{
                    connected = false;
                    return new Response.Builder().type(ResponseType.ERROR).data("Invalid username or password").build();
                }
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.LOGOUT) {
            System.out.println("Logout request ...");
            String username = (String) request.getData();
            try {
                server.logout(username);
                return okResponse;
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GET_CHALLENGES) {
            System.out.println("findAllChallenges request ...");
            try {
                return new Response.Builder().type(ResponseType.GET_CHALLENGES).data(server.findAllChallenges()).build();
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.GET_PARTICIPANTS_BY_CHALLENGE_TYPE_AGE_CATEGORY) {
            System.out.println("findParticipantsByChallengeTypeAndAgeCategory request ...");
            var filter = (ChallengeDto) request.getData();
            try {
                return new Response.Builder().type(ResponseType.GET_PARTICIPANTS_BY_CHALLENGE_TYPE_AGE_CATEGORY).data(server.findParticipantsByChallengeTypeAndAgeCategory(filter.getType(), filter.getAgeCategory())).build();
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType() == RequestType.ADD_PARTICIPANT) {
            System.out.println("addParticipant request ...");
            ParticipationInfoDto addition = (ParticipationInfoDto) request.getData();
            try {
                server.addParticipant(addition.getName(), addition.getAge(), addition.getType());
                return okResponse;
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) {
        try {
            output.writeObject(response);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void participationAdded() {
        Response response = new Response.Builder().type(ResponseType.UPDATE).data(null).build();
        sendResponse(response);
    }
}
