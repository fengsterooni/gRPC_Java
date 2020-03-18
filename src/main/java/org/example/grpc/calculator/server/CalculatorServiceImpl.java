package org.example.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        SumResponse response = SumResponse.newBuilder()
                .setResult(request.getFirst() + request.getSecond())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void primeDecomp(PrimeDecompRequest request, StreamObserver<PrimeDecompResponse> responseObserver) {
        int number = request.getNumber();

        int factor = 2;
        while (number > 1) {
            if (number % factor == 0) {
                PrimeDecompResponse primeDecompResponse = PrimeDecompResponse.newBuilder()
                        .setResult(factor)
                        .build();

                responseObserver.onNext(primeDecompResponse);
                number /= factor;
            } else {
                factor++;
            }
        }

        responseObserver.onCompleted();

    }

    @Override
    public StreamObserver<AverageRequest> average(StreamObserver<AverageResponse> responseObserver) {

        StreamObserver<AverageRequest> requestObserver = new StreamObserver<AverageRequest>() {

            int sum = 0;
            int count = 0;

            @Override
            public void onNext(AverageRequest value) {
                sum += value.getNumber();
                count++;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                double average = 1.0 * sum / count;

                responseObserver.onNext(
                        AverageResponse.newBuilder()
                                .setResult(average)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }
}
