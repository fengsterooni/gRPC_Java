package org.example.grpc.blog.client;

import com.proto.blog.*;
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

        String blogId = createBlogResponse.getBlog().getId();

        System.out.println("Reading blog...");

        ReadBlogResponse readBlogResponse = blockingStub.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());

        System.out.println(readBlogResponse.toString());

//        System.out.println("Reading blog not existing");
//        ReadBlogResponse readBlogResponseNotFound = blockingStub.readBlog(ReadBlogRequest.newBuilder()
//                .setBlogId("5e746624e4a86700a47e7381")
//                .build());

        Blog newBlog = Blog.newBuilder()
                .setId(blogId)
                .setAuthorId("New Author")
                .setTitle("New Blog (updated)")
                .setContent("Hello World! (updated) with new content")
                .build();

        System.out.println("Updating blog...");
        UpdateBlogResponse updateBlogResponse = blockingStub.updateBlog(
                UpdateBlogRequest.newBuilder()
                .setBlog(newBlog)
                .build());

        System.out.println("Updated blog...");
        System.out.println(updateBlogResponse.toString());

        System.out.println("Deleting blog...");
        DeleteBlogResponse deleteBlogResponse = blockingStub.deleteBlog(
                DeleteBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build()
        );

        System.out.println("Deleted blog...");

//        ReadBlogResponse readBlogResponseAfterDeletion = blockingStub.readBlog(ReadBlogRequest.newBuilder()
//                .setBlogId(blogId)
//                .build());

        blockingStub.listBlog(ListBlogRequest.newBuilder().build())
            .forEachRemaining(listBlogResponse ->
                    System.out.println(listBlogResponse.getBlog().toString()));
    }
}
