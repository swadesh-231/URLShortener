package com.urlshortener.service;

import com.urlshortener.dto.ClickDTO;
import com.urlshortener.dto.MappingDTO;
import com.urlshortener.model.UrlMapping;
import com.urlshortener.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MappingService {
    MappingDTO createShortUrl(String originalUrl, User user);
    List<MappingDTO> getUrlByUser(User user);
    List<ClickDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end);
    Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end);
    UrlMapping getOriginalUrl(String shortUrl);
}
