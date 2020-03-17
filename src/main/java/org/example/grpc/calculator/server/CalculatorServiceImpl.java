package org.example.grpc.calculator.server;

import com.proto.calculator.*;
import com.proto.greet.GreetManyTimesResponse;
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
}
