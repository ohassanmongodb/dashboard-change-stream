package com.example.demo.service;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import org.bson.BsonNull;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DashboardDTO;
import com.example.demo.repository.EmailRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {
    private final EmailRepository emailRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private MongoTemplate mongoTemplate;

    @Autowired
    public DashboardService(EmailRepository emailRepository,
                            SimpMessagingTemplate messagingTemplate,
                            MongoTemplate mongoTemplate) {
        this.emailRepository = emailRepository;
        this.messagingTemplate = messagingTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    public DashboardDTO getDashboardData() {

     //  long readCount = emailRepository.countByStatus("READ");
     //  long unreadCount = emailRepository.countByStatus("UNREAD");
     //  long urgentCount = emailRepository.countByUrgentTrue();
     //  return new DashboardDTO(readCount, unreadCount, urgentCount);
        MongoCollection<Document> collection = mongoTemplate.getCollection("emailDashboard");

        Document first = collection.find().first();
        long read = first.getLong("read");
        long total = first.getLong("total");
        long unread = first.getLong("unread");

        return new DashboardDTO(read,unread,total);

    }

    public void publishDashboardUpdate() {
        DashboardDTO dto = getDashboardData();
        messagingTemplate.convertAndSend("/topic/dashboard", dto);
    }
}