package monto.broker.websocket;

import java.net.InetSocketAddress;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.zeromq.ZContext;

public class Main {

  private static Options options = new Options();
  private static HelpFormatter hf = new HelpFormatter();

  public static void main(String[] args) throws ParseException, InterruptedException {
    options
        .addOption("source", true, "set zeromq source address")
        .addOption("sink", true, "set zeromq sink address")
        .addOption("debug", false, "enables debug output");

    String srcAddress = null;
    String snkAddress = null;

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    if (cmd.hasOption("source")) {
      srcAddress = cmd.getOptionValue("source");
    } else {
      exit();
    }

    if (cmd.hasOption("sink")) {
      snkAddress = cmd.getOptionValue("sink");
    } else {
      exit();
    }

    boolean debug = cmd.hasOption("debug");

    ZContext context = new ZContext(1);
    Thread wsd =
        new Thread(
            new WebSocketSendProxy(new InetSocketAddress(5003), srcAddress, context, debug),
            "send-proxy");
    wsd.start();
    WebSocketReceiveProxy receiver =
        new WebSocketReceiveProxy(new InetSocketAddress(5004), snkAddress, context, debug);
    receiver.start();
  }

  private static void exit() {
    hf.printHelp("", "", options, "", true);
    System.exit(0);
  }
}
