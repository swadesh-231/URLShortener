package com.urlshortener.repository;

import com.urlshortener.model.UrlMapping;
import com.urlshortener.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends CrudRepository<UrlMapping, Long> {
    UrlMapping findBySortUrl(String sortUrl);
    List<UrlMapping> findByUser(User user);
}
