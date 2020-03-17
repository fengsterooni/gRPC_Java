package org.example.grpc.sum.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {
    public static void main(String[] args) {
        System.out.println("Hello, I am gRPC sum client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        System.out.println("Creating stub");

        SumServiceGrpc.SumServiceBlockingStub sumClient = SumServiceGrpc.newBlockingStub(channel);

        SumRequest sumRequest = SumRequest.newBuilder()
                .setFirst(10)
                .setSecond(3)
                .build();

        SumResponse sumResponse = sumClient.sum(sumRequest);

        System.out.println(sumResponse.getResult());

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
