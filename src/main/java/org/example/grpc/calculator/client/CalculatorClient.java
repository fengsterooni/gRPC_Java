package org.example.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
    public static void main(String[] args) {
        System.out.println("Hello, I am gRPC sum client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        System.out.println("Creating stub");

        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        SumRequest sumRequest = SumRequest.newBuilder()
                .setFirst(10)
                .setSecond(3)
                .build();

        SumResponse sumResponse = stub.sum(sumRequest);

        System.out.println(sumResponse.getResult());

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}