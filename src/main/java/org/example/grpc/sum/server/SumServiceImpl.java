package org.example.grpc.sum.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
//        super.sum(request, responseObserver);

        SumResponse response = SumResponse.newBuilder()
                .setResult(request.getFirst() + request.getSecond())
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}
