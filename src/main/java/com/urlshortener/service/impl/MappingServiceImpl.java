package com.urlshortener.service.impl;

import com.urlshortener.dto.ClickDTO;
import com.urlshortener.dto.MappingDTO;
import com.urlshortener.model.UrlMapping;
import com.urlshortener.model.User;
import com.urlshortener.repository.ClickRepository;
import com.urlshortener.repository.UrlMappingRepository;
import com.urlshortener.service.MappingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MappingServiceImpl implements MappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final ClickRepository clickRepository;
    private final ModelMapper modelMapper;

    @Override
    public MappingDTO createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setSortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());

        UrlMapping saved = urlMappingRepository.save(urlMapping);
        return convertToDto(saved);
    }

    @Override
    public List<MappingDTO> getUrlByUser(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public List<ClickDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findBySortUrl(shortUrl);
        if (urlMapping == null) {
            return Collections.emptyList();
        }
        return clickRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end)
                .stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> ClickDTO.builder()
                        .clickDate(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        return Map.of();
    }

    @Override
    public UrlMapping getOriginalUrl(String shortUrl) {
        return null;
    }
    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }
    private MappingDTO convertToDto(UrlMapping urlMapping) {
        MappingDTO mappingDTO = new MappingDTO();
        mappingDTO.setId(urlMapping.getId());
        mappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        mappingDTO.setShortUrl(urlMapping.getSortUrl());
        mappingDTO.setClickCount(urlMapping.getClickCount());
        mappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        mappingDTO.setUsername(urlMapping.getUser().getUsername());
        return mappingDTO;

    }
}
