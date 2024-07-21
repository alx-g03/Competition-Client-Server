package org.example.network.utils;

import org.example.network.rpcprotocol.UserRpcReflectionWorker;
import org.example.services.Services;

import java.net.Socket;

public class RpcConcurrentServer extends AbstractConcurrentServer {
    private Services services;

    public RpcConcurrentServer(int port, Services services) {
        super(port);
        this.services = services;
        System.out.println("RPCConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        UserRpcReflectionWorker worker = new UserRpcReflectionWorker(services, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
