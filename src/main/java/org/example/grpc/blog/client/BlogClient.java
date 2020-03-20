package org.example.grpc.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class BlogClient {
    public static void main(String[] args) {
        System.out.println("Hello, I am gRPC sum client");

        BlogClient client = new BlogClient();

        System.out.println("Creating stub");

        client.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        BlogServiceGrpc.BlogServiceBlockingStub blockingStub = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setAuthorId("Author")
                .setTitle("New Blog")
                .setContent("Hello World!")
                .build();

        CreateBlogResponse createBlogResponse = blockingStub.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build()
        );

        System.out.println("Received create blog response");
        System.out.println(createBlogResponse.toString());

//        System.out.println("Shutting down channel");
//        channel.shutdown();
    }

}
