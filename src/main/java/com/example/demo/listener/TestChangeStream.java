package com.example.demo.listener;

import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.BsonNull;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TestChangeStream implements CommandLineRunner {

    @Autowired
    private MongoClient mongoClient;

    @Override
    public void run(String... args) {
        new Thread(this::startChangeStream).start();
    }

    private void startChangeStream() {
        System.out.println("Listening test change stream...");
        MongoDatabase database = mongoClient.getDatabase("demo");
        MongoCollection<Document> collection = database.getCollection("email");

        MongoCursor<ChangeStreamDocument<Document>> cursor =
                collection.watch().iterator();

        System.out.println("Change Stream aperto su 'demo.email'. Attendo eventi...");
        while (cursor.hasNext()) {
            ChangeStreamDocument<Document> event = cursor.next();
            Document full = event.getFullDocument();
            System.out.println("Nuovo evento: " + full);

            AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$group",
                            new Document("_id",
                                    new BsonNull())
                                    .append("read",
                                            new Document("$sum",
                                                    new Document("$cond", Arrays.asList(new Document("$eq", Arrays.asList("$status", "READ")), 1L, 0L))))
                                    .append("unread",
                                            new Document("$sum",
                                                    new Document("$cond", Arrays.asList(new Document("$eq", Arrays.asList("$status", "UNREAD")), 1L, 0L))))
                                    .append("total",
                                            new Document("$sum", 1L))),
                    new Document("$set",
                            new Document("_id", "emailDashboard")),
                    new Document("$out", "emailDashboard")));
            result.toCollection();
            System.out.println("collection aggiornata");

        }
    }
}
