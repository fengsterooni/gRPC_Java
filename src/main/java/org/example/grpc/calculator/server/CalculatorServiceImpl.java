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
}
