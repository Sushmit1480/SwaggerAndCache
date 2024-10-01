package com.swaggerExample.swaggerExample.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheManagerService {

    @CacheEvict(value = "employees", allEntries = true)
    @Scheduled(fixedRate = 600000) // 10 minutes
    public void evictAllCacheValues() {
        System.out.println("Clearing all employees cache");
    }
}
