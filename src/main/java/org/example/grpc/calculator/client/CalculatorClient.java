package org.example.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    public static void main(String[] args) {
        System.out.println("Hello, I am gRPC sum client");

        CalculatorClient client = new CalculatorClient();

        System.out.println("Creating stub");

        client.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

//        doSumCall(channel);

//        doPrimeDecompCall(channel);

        doAverageCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private void doSumCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        SumRequest sumRequest = SumRequest.newBuilder()
                .setFirst(10)
                .setSecond(3)
                .build();

        SumResponse sumResponse = stub.sum(sumRequest);

        System.out.println(sumResponse.getResult());
    }

    private void doPrimeDecompCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);

        Integer number = 1543251345;

        stub.primeDecomp(PrimeDecompRequest.newBuilder()
                .setNumber(number).build())
                .forEachRemaining(primeDecompResponse ->
                        System.out.println(primeDecompResponse.getResult()));

    }

    private void doAverageCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<AverageRequest> requestObserver = asyncClient.average(new StreamObserver<AverageResponse>() {
            @Override
            public void onNext(AverageResponse value) {
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        for (int i = 0; i < 10000; i++) {
            requestObserver.onNext(AverageRequest.newBuilder()
                    .setNumber(i)
                    .build());
        }

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
