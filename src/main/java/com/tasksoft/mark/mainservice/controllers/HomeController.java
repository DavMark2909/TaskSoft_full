package com.tasksoft.mark.mainservice.controllers;

import com.tasksoft.mark.mainservice.dto.HomeDashboardDTO;
import com.tasksoft.mark.mainservice.dto.SecurityUserDto;
import com.tasksoft.mark.mainservice.security.SecurityUtils;
import com.tasksoft.mark.mainservice.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final SecurityUtils securityUtils;
    private final HomeService homeService;

    public HomeController(SecurityUtils securityUtils, HomeService homeService) {
        this.securityUtils = securityUtils;
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public ResponseEntity<HomeDashboardDTO> home(){
        Long userId = securityUtils.getCurrentUserId();
        HomeDashboardDTO dashboardData = homeService.getUserDashboard(userId);

        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/user")
    public ResponseEntity<SecurityUserDto> user(){
        String userName = securityUtils.getCurrentUserName();
        Long userId = securityUtils.getCurrentUserId();
        String role = securityUtils.getCurrentUserRole();
        SecurityUserDto dto = new SecurityUserDto(userName, userId, role);
        return ResponseEntity.ok(dto);
    }
}
