package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DashboardDTO;
import com.example.demo.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {
    private final DashboardService dashboardService;

    @Autowired
    public DashboardRestController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboardData();
    }
}