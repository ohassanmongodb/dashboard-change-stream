package com.example.demo.dto;

public class DashboardDTO {
    private long readCount;
    private long unreadCount;
    private long urgentCount;

    public DashboardDTO() {}

    public DashboardDTO(long readCount, long unreadCount, long urgentCount) {
        this.readCount = readCount;
        this.unreadCount = unreadCount;
        this.urgentCount = urgentCount;
    }

    public long getReadCount() {
        return readCount;
    }
    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }
    public long getUnreadCount() {
        return unreadCount;
    }
    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
    public long getUrgentCount() {
        return urgentCount;
    }
    public void setUrgentCount(long urgentCount) {
        this.urgentCount = urgentCount;
    }
}