package org.example.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello, I am gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).build();
        System.out.println("Creating stub");
        DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
//        DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();

    }
}
