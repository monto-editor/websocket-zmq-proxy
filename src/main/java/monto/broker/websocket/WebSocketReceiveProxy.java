package monto.broker.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class WebSocketReceiveProxy extends WebSocketServer {

  private ZContext context;
  private ZMQ.Socket socket;
  private boolean debug;
  private List<WebSocket> webSockets;
  private boolean running;
  private Object webSocketAddress;
  private String zmqAddress;

  public WebSocketReceiveProxy(
      InetSocketAddress webSocketAddress, String zmqAddress, ZContext context, boolean debug) {
    super(webSocketAddress);
    this.context = context;
    this.webSocketAddress = webSocketAddress;
    this.zmqAddress = zmqAddress;
    this.debug = debug;
    connect(zmqAddress);
    webSockets = new ArrayList<>();
  }

  private void connect(String address) {
    socket = context.createSocket(ZMQ.PAIR);
    socket.connect(address);
    System.out.printf("receive proxy: %s -> %s\n", address, webSocketAddress);
  }

  @Override
  public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    webSockets.add(webSocket);
  }

  @Override
  public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    webSockets.remove(webSocket);
  }

  @Override
  public void onMessage(WebSocket webSocket, String s) {}

  @Override
  public void onError(WebSocket webSocket, Exception e) {
    e.printStackTrace();
  }

  @Override
  public void start() {
    super.start();
    running = true;
    while (running) {
      String msg = socket.recvStr();
      if (webSockets.size() > 0 && msg != null) {
        for (WebSocket webSocket : webSockets) {
          if (debug) {
            System.out.printf("zmq %s -> websocket %s: %s\n", zmqAddress, webSocketAddress, msg);
          }
          webSocket.send(msg);
        }
      }
    }
  }

  @Override
  public void stop() throws IOException, InterruptedException {
    super.stop();
    running = false;
  }
}
