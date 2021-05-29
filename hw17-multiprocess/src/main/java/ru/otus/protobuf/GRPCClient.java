package ru.otus.protobuf;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

import com.google.protobuf.Int64Value;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteGeneratorServiceGrpc;
import ru.otus.protobuf.generated.SequenceParameters;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        RemoteGeneratorServiceGrpc.RemoteGeneratorServiceStub newStub = RemoteGeneratorServiceGrpc.newStub(channel);

        SequenceParameters params = SequenceParameters.newBuilder()
                                                      .setRange( SequenceParameters.Range.newBuilder()
                                                                                         .setFrom(0)
                                                                                         .setTo(30)
                                                                                         .build() )
                                                      .setDelayMs(2000)
                                                      .build();


        final var serverValueQueue = new ConcurrentLinkedDeque<Long>();

        newStub.generateSequence( params , new StreamObserver<Int64Value>() {
            @Override
            public void onNext(Int64Value l) {
                System.out.println(String.format("  new value %d", l.getValue() ));
                serverValueQueue.poll();
                serverValueQueue.add(l.getValue());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("\n\n request completed");
            }
        });


        final var currentAddition = new AtomicLong(0L);

        LongStream.range(0, 50).forEach( l -> {
            currentAddition.set( currentAddition.get() + Optional.ofNullable( serverValueQueue.poll() ).orElse(0L) );
            System.out.println( String.format( "current value %d ", l + currentAddition.get() ));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        });


        channel.shutdown();
    }
}
