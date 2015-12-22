package monto.broker.websocket;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

public class WebSocketPublisherProxy extends WebSocketServer {

    private ZContext context;
    private boolean debug;
    private Socket socket;
	private InetSocketAddress webSocketAddress;
	private String zmqAddress;

    public WebSocketPublisherProxy(InetSocketAddress webSocketAddress, String zmqAddress, ZContext context, boolean debug) {
        super(webSocketAddress);
        this.webSocketAddress = webSocketAddress;
        this.zmqAddress = zmqAddress;
        this.context = context;
        this.debug = debug;
        connect(zmqAddress);
    }

    private void connect(String address) {
        socket = context.createSocket(ZMQ.PUB);
        socket.connect(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        if(debug)
        	System.out.printf("websocket %s -> zmq %s: %s\n", webSocketAddress, zmqAddress, s);
        socket.send(s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }
}
