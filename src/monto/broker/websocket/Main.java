package monto.broker.websocket;

import org.apache.commons.cli.*;
import org.zeromq.ZContext;

import java.net.InetSocketAddress;

public class Main {

    private static Options options = new Options();
    private static HelpFormatter hf = new HelpFormatter();

    public static void main(String[] args) throws ParseException {
        options.addOption("source", true, "set zeromq source address")
                .addOption("sink", true, "set zeromq sink address")
                .addOption("discovery", true, "set zeromq discovery address")
                .addOption("configuration", true, "set zeromq configuration address")
                .addOption("debug", false, "enables debug output");

        String srcAddress = null;
        String snkAddress = null;
        String discAddress = null;
        String configAddress = null;

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

        if (cmd.hasOption("discovery")) {
            discAddress = cmd.getOptionValue("discovery");
        } else {
            exit();
        }

        if (cmd.hasOption("configuration")) {
            configAddress = cmd.getOptionValue("configuration");
        } else {
            exit();
        }

        boolean debug = cmd.hasOption("debug");

        ZContext context = new ZContext(1);
        Thread wsd = new Thread(new WebSocketRequestProxy(new InetSocketAddress(5006), discAddress, context, debug));
        wsd.start();
        Thread wsc = new Thread(new WebSocketPublisherProxy(new InetSocketAddress(5008), configAddress, context, debug));
        wsc.start();
        Thread wsp = new Thread(new WebSocketPublisherProxy(new InetSocketAddress(5002), srcAddress, context, debug));
        wsp.start();
        WebSocketSubscriberProxy wss = new WebSocketSubscriberProxy(new InetSocketAddress(5003), snkAddress, context, debug);
        wss.start();
    }

    private static void exit() {
        hf.printHelp("", "", options, "", true);
        System.exit(0);
    }
}
