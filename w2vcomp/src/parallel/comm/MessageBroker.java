package parallel.comm;

import java.io.IOException;
import java.util.Date;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class MessageBroker implements Runnable {

    private int        aggregatorPort;
    private int        estimatorsPort;

    private static int controlPort = 5555;

    public MessageBroker(int aggregatorPort, int estimatorsPort) {
        this.aggregatorPort = aggregatorPort;
        this.estimatorsPort = estimatorsPort;

    }

    public String getControlEndpoint() {
        try {
            String hostname = NetUtils.getHostname();
            return "tcp://" + hostname + ":" + MessageBroker.controlPort;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't determine current hostname");
        }
    }

    public void stop(Context context) {
        Socket control = context.socket(ZMQ.PUSH);
        System.out.println("Connecting to control socket");
        control.connect(getControlEndpoint());
        byte[] msg = new byte[1];
        msg[0] = 0;
        System.out.println("Sending stop signal");
        control.send(msg, 0);
        System.out.println("Closing");
        control.close();
    }

    @Override
    public void run() {
        System.out.println("launch and connect broker.");
        Context context = ZMQ.context(1);
        // Socket that will communicate in the direction of the star destroyer
        Socket paramsAggregatorSocket = context.socket(ZMQ.DEALER);
        // Socket that will communicate in the direction of the tie fighters
        Socket paramEstimatorsSocket = context.socket(ZMQ.ROUTER);
        // Allow us to break out from the msg forwarding loop
        Socket control = context.socket(ZMQ.PULL);

        paramsAggregatorSocket.bind("tcp://*:" + aggregatorPort);
        paramEstimatorsSocket.bind("tcp://*:" + estimatorsPort);
        control.bind("tcp://*:" + controlPort);

        // Initialize poll set
        Poller items = new Poller(2);
        items.register(paramEstimatorsSocket, Poller.POLLIN);
        items.register(paramsAggregatorSocket, Poller.POLLIN);
        items.register(control, Poller.POLLIN);

        boolean more = false;
        byte[] message;
        int aggregator_requests = 0;
        long prev_time = new Date().getTime();
        long idle_time = 0, active_time = 0;

        System.out.println("Initiating communication proxy");
        // Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
            // poll and memorize multipart detection
            items.poll();

            System.out.println("Message received");

            long new_time = new Date().getTime();
            if (aggregator_requests == 0) {
                idle_time += new_time - prev_time;
            } else {
                active_time += new_time - prev_time;
            }
            prev_time = new_time;

            System.out.println("Time aggregator spent: idle ["
                    + ((100 * idle_time) / (idle_time + active_time))
                    + "%] active [" + ((100 * active_time)
                    / (idle_time + active_time)) + "%]");

            if (items.pollin(0)) {
                System.out.println("estimator socket");
                aggregator_requests += 1;
                System.out.println("Total aggregator requests: "
                        + aggregator_requests);
                long msgSize = 0;
                while (true) {
                    // receive message
                    message = paramEstimatorsSocket.recv(0);
                    msgSize += message.length;
                    more = paramEstimatorsSocket.hasReceiveMore();

                    // Broker it
                    paramsAggregatorSocket
                            .send(message, more ? ZMQ.SNDMORE : 0);
                    if (!more) {
                        System.out.println("Forwarded " + String.format("%,d",msgSize) + " bytes");
                        break;
                    }
                }
            }
            if (items.pollin(1)) {
                System.out.println("agg socket");
                aggregator_requests -= 1;
                System.out.println("Total aggregator requests: "
                        + aggregator_requests);
                long msgSize = 0;
                while (true) {
                    // receive message
                    message = paramsAggregatorSocket.recv(0);
                    msgSize += message.length;
                    more = paramsAggregatorSocket.hasReceiveMore();
                    // Broker it
                    paramEstimatorsSocket.send(message, more ? ZMQ.SNDMORE : 0);
                    if (!more) {
                        System.out.println("Forwarded " + String.format("%,d",msgSize) + " bytes");
                        break;
                    }
                }
            }
            if (items.pollin(2)) {
                System.out.println("Received stop command");
                byte[] cmd = control.recv();
                if (cmd[0] == 0) {
                    break;
                }
            }
        }

        System.out.println("Closing proxy sockets");
        paramEstimatorsSocket.close();
        paramsAggregatorSocket.close();
        control.close();
        System.out.println("Terminating");
        context.term();
        System.out.println("Done");
        control.close();
    }

}
