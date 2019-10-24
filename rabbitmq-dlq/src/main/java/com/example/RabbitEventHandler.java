package com.example;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

class RabbitEventHandler {

    private static final String QUEUE_NAME = "test";
    private static final ConnectionFactory factory = new ConnectionFactory();
    private static final Logger logger = LoggerFactory.getLogger(RabbitEventHandler.class);

    public static void main(String[] arg) throws IOException, TimeoutException {
        createPublisher();
        createConsumer();
    }

    private static void createPublisher() throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        AtomicInteger counter = new AtomicInteger();
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(() -> {
            try {
                channel.basicPublish("", QUEUE_NAME,
                        new AMQP.BasicProperties.Builder()
                                .build(), ("Message " + counter.getAndIncrement()).getBytes());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private static void createConsumer() {
        Connection connection;
        Channel channel;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            Map<String, Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange", "");
            args.put("x-dead-letter-routing-key", QUEUE_NAME + ".dlq");
            channel.queueDeclare(QUEUE_NAME + ".dlq", true, false, false, null);
            channel.queueDeclare(QUEUE_NAME, true, false, false, args);

            DeliverCallback deliverCallback = (consumerTag, message) -> {
                try {
                    handleMessage(new String(message.getBody()));
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                } catch (Exception ex) {
                    channel.basicNack(message.getEnvelope().getDeliveryTag(), false, false);
                    logger.error("{}", ex);
                }
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    private static void handleMessage(String message) {
        if (Integer.parseInt(message.split(" ")[1]) / 2 != 0) {
            logger.info("Rejecting message {}", message);
            throw new RuntimeException() {};
        }
        logger.info("Handled message {}", message);
    }
}
