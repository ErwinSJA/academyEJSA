package com.ejsa.academy.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLoader {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> List<T> loadObjects(String filePath, Class<T> clazz) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
