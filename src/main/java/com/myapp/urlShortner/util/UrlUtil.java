package com.myapp.urlShortner.util;

import com.example.user_service.exception.ResourceNotFoundException;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class UrlUtil {

    public static String normalize(String url) {

        //url != null && !url.isBlank()
        if (ObjectUtils.isEmpty(url)) {
            throw new ResourceNotFoundException("URL cannot be empty");
        }

        url = url.trim().replace("/", "");

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        return url;
    }
}
