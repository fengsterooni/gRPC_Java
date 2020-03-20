package org.example.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.internal.bulk.DeleteRequest;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    public static final String BLOG_AUTHOR_ID = "author_id";
    public static final String BLOG_TITLE = "title";
    public static final String BLOG_CONTENT = "content";
    public static final String BLOG_ID = "_id";

    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase database = mongoClient.getDatabase("mydb");
    private MongoCollection<Document> collection = database.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

        System.out.println("Received Create Blog request");
        Blog blog = request.getBlog();

        Document doc = new Document(BLOG_AUTHOR_ID, blog.getAuthorId())
                .append(BLOG_TITLE, blog.getTitle())
                .append(BLOG_CONTENT, blog.getContent());

        System.out.println("Inserting blog...");

        collection.insertOne(doc);

        String id = doc.getObjectId(BLOG_ID).toString();

        System.out.println("Inserted blog: " + id);

        CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(blog.toBuilder().setId(id).build())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
        System.out.println("Received Read Blog request");
        String blogId = request.getBlogId();
        System.out.println("Searching blog " + blogId);

        Document result = null;

        try {
            result = collection.find(eq(BLOG_ID, new ObjectId(blogId)))
                    .first();
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with the corresponding id was not found")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException()
            );
        }

        if (result == null) {
            System.out.println("Blog not found!");
            responseObserver.onError(Status.NOT_FOUND
            .withDescription("The blog with the corresponding id was not found")
            .asRuntimeException()
            );
        } else {
            System.out.println("Blog found, sending response");
            Blog blog = documentToBlog(result);

            responseObserver.onNext(ReadBlogResponse.newBuilder()
                    .setBlog(blog)
                    .build());

            responseObserver.onCompleted();
        }
    }

    private Blog documentToBlog(Document document) {
        return Blog.newBuilder()
                .setAuthorId(document.getString(BLOG_AUTHOR_ID))
                .setTitle(document.getString(BLOG_TITLE))
                .setContent(document.getString(BLOG_CONTENT))
                .setId(document.getObjectId(BLOG_ID).toString())
                .build();
    }

    @Override
    public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {
        System.out.println("Received Update Blog request");

        Blog blog = request.getBlog();
        String blogId = blog.getId();

        System.out.println("Searching blog to update it");

        Document result = null;

        try {
            result = collection.find(eq(BLOG_ID, new ObjectId(blogId)))
                    .first();
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with the corresponding id was not found")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException()
            );
        }

        if (result == null) {
            System.out.println("Blog not found!");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with the corresponding id was not found")
                    .asRuntimeException()
            );
        } else {
            Document replacement = new Document(BLOG_AUTHOR_ID, blog.getAuthorId())
                    .append(BLOG_TITLE, blog.getTitle())
                    .append(BLOG_CONTENT, blog.getContent())
                    .append(BLOG_ID, new ObjectId(blogId));

            System.out.println("Replacing blog in database");

            collection.replaceOne(eq(BLOG_ID, result.getObjectId(BLOG_ID)), replacement);

            System.out.println("Replaced, sending response");

            responseObserver.onNext(
                    UpdateBlogResponse.newBuilder()
                    .setBlog(documentToBlog(replacement))
                    .build());

            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteBlog(DeleteBlogRequest request, StreamObserver<DeleteBlogResponse> responseObserver) {
        System.out.println("Received Delete Blog request");
        String blogId = request.getBlogId();
        DeleteResult result = null;

        try {
            result = collection.deleteOne(eq(BLOG_ID, new ObjectId(blogId)));
        } catch (Exception e) {
            System.out.println("Blog not found");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with the corresponding id was not found")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException()
            );
        }

        if (result.getDeletedCount() == 0) {
            System.out.println("Blog not found");
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("The blog with the corresponding id was not found")
                    .asRuntimeException()
            );
        } else {
            System.out.println("Blog deleted");
            responseObserver.onNext(DeleteBlogResponse.newBuilder()
                    .setBlogId(blogId)
                    .build());

            responseObserver.onCompleted();
        }
    }
}
