package com.example.demo.listener;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import com.example.demo.service.DashboardService;

import javax.annotation.PreDestroy;

@Component
public class EmailChangeStreamListener implements SmartLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(EmailChangeStreamListener.class);
    
    private final MongoTemplate mongoTemplate;
    private final DashboardService dashboardService;
    private volatile boolean running = false;
    private Thread changeStreamThread;

    @Autowired
    public EmailChangeStreamListener(MongoTemplate mongoTemplate,
                                   DashboardService dashboardService) {
        this.mongoTemplate = mongoTemplate;
        this.dashboardService = dashboardService;
    }

    @Override
    public void start() {
        if (!this.running) {
            this.running = true;
            this.changeStreamThread = new Thread(this::watchForChanges);
            this.changeStreamThread.setDaemon(true);
            this.changeStreamThread.start();
            logger.info("Started MongoDB change stream listener");
        }
    }
    
    private void watchForChanges() {
        MongoCollection<Document> collection = mongoTemplate.getCollection("emailDashboard");
        
        while (running) {
            try (MongoCursor<ChangeStreamDocument<Document>> cursor = collection.watch().iterator()) {
                logger.info("Watching for changes in email collection...");
                
                while (running && cursor.hasNext()) {
                    ChangeStreamDocument<Document> change = cursor.next();
                    logger.debug("Received change event: {}", change);
                    dashboardService.publishDashboardUpdate();
                }
            } catch (Exception e) {
                if (running) {
                    logger.error("Error in change stream. Will attempt to reconnect...", e);
                    try {
                        Thread.sleep(5000); // Wait before reconnecting
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    @Override
    @PreDestroy
    public void stop() {
        this.running = false;
        if (this.changeStreamThread != null) {
            this.changeStreamThread.interrupt();
            try {
                this.changeStreamThread.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("Stopped MongoDB change stream listener");
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
    
    @Override
    public boolean isAutoStartup() {
        return true;
    }
    
    @Override
    public void stop(@NonNull Runnable callback) {
        stop();
        callback.run();
    }
    
    @Override
    public int getPhase() {
        return 0;
    }
}