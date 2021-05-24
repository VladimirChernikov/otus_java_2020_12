package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteGeneratorServiceGrpc;
import ru.otus.protobuf.generated.SequenceParameters;
import java.util.List;
import java.util.stream.LongStream;
import com.google.protobuf.Int64Value;

public class RemoteGeneratorServiceImpl extends RemoteGeneratorServiceGrpc.RemoteGeneratorServiceImplBase {

    public RemoteGeneratorServiceImpl() {
    }

    @Override
    public void generateSequence(SequenceParameters request, StreamObserver<Int64Value> responseObserver) {
        LongStream.range( request.getRange().getFrom(), request.getRange().getTo() )
            .forEach(l -> {
                    responseObserver.onNext( Int64Value.newBuilder().setValue(l).build() );
                    try {
                        Thread.sleep( request.getDelayMs() );
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                });
        responseObserver.onCompleted();
    }

}
