package com.myapp.urlShortner.service;

import com.example.user_service.exception.ResourceNotFoundException;
import com.myapp.urlShortner.entity.UrlMapping;
import com.myapp.urlShortner.repository.UrlRepository;
import com.myapp.urlShortner.util.Base62Util;
import com.myapp.urlShortner.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public String createShortUrl(String longUrl) {

        // code to use uuid
       /* longUrl = UrlUtil.normalize(longUrl);
        String shortCode = generateShortCode();

        UrlMapping url = new UrlMapping();
        url.setLongUrl(longUrl);
        url.setShortCode(shortCode);
        url.setClickCount(0);

        urlRepository.save(url);

        return shortCode;
        */

        // code to use base62
        longUrl = UrlUtil.normalize(longUrl);

        UrlMapping url = new UrlMapping();
        url.setLongUrl(longUrl);
        url.setClickCount(0);

        // First save to get ID
        url = urlRepository.save(url);

        // Convert ID → Base62
        String shortCode = Base62Util.encode(url.getId());
        String sixDigitCode = toFixedLength(shortCode);

        url.setShortCode(sixDigitCode);
        urlRepository.save(url);

        return sixDigitCode;
    }

    @Cacheable(value = "urls", key = "#shortCode")
    public String getLongUrl(String shortCode) {
        UrlMapping url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found"));

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getLongUrl();
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    /* private String toFixedLength(String shortCode) {
        int length = 6;
        StringBuilder sb = new StringBuilder(shortCode);

        while (sb.length() < length) {
            sb.insert(0, '0'); // padding
        }

        return sb.toString();
    }
*/
    private String toFixedLength(String shortCode) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder sb = new StringBuilder(shortCode);

        while (sb.length() < 6) {
            sb.insert(0, chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
