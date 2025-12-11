package com.urlshortener.controller;

import com.urlshortener.dto.MappingDTO;
import com.urlshortener.model.User;
import com.urlshortener.service.MappingService;
import com.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/url")
@RequiredArgsConstructor
public class MappingController {
    private final MappingService mappingService;
    private final UserService userService;

    @PostMapping("/shorturl")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MappingDTO> createShortUrl(@RequestBody Map<String, String> request, Principal principal) {
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        MappingDTO mappingDTO = mappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(mappingDTO);
    }
    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<MappingDTO>> getUserUrls(Principal principal){
        User user = userService.findByUsername(principal.getName());
        List<MappingDTO> urls = mappingService.getUrlByUser(user);
        return ResponseEntity.ok(urls);
    }

}
