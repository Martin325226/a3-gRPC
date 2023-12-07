package Server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051; // Default port
        Server server = ServerBuilder.forPort(port)
                .addService(new ForumServiceImpl())
                .build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started on port " + port);
        server.awaitTermination();
    }
}
