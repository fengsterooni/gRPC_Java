package org.example.grpc.blog.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class BlogClient {
    public static void main(String[] args) {
        System.out.println("Hello, I am gRPC sum client");

        BlogClient client = new BlogClient();

        System.out.println("Creating stub");

        client.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

//        doSumCall(channel);

//        doPrimeDecompCall(channel);

//        doAverageCall(channel);

//        doMaxCall(channel);

//        doErrorCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

}
