package com.example.accountingforsocks.services.impl;

import com.example.accountingforsocks.exception.FileException;
import com.example.accountingforsocks.model.Socks;
import com.example.accountingforsocks.services.FileService;
import com.example.accountingforsocks.services.WarehouseServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class WarehouseServicesImpl implements WarehouseServices {
    final private FileService fileService;
    private static Map<Socks, Long> socksWarehouse = new HashMap<>();


    public WarehouseServicesImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    private void init() {
        try {
            readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Socks addSocks(Socks socks1, Long count) {
        if (socksWarehouse.containsKey(socks1)) {
            Long c = count + socksWarehouse.get(socks1);
            socksWarehouse.put(socks1, c);
            saveToFile();
        } else {
            socksWarehouse.put(socks1, count);
            saveToFile();

        }
        return socks1;
    }


    @Override
    public Long sendSocks(Socks socks1, Long count) {
        if (socksWarehouse.containsKey(socks1) && socksWarehouse.get(socks1) >= count) {
            Long c = socksWarehouse.get(socks1) - count;
            socksWarehouse.put(socks1, c);
            saveToFile();
        } else {
            return 0L;
        }
        return count;
    }


    @Override
    public Long getSocksCount(Socks socks1) {
        Long a = 0L;
        if (socksWarehouse.containsKey(socks1)) {
            a = socksWarehouse.get(socks1);
        }
        return a;
    }

    @Override
    public boolean deleteSocks(Socks socks) {
        if (socksWarehouse.containsKey(socks)) {
            socksWarehouse.remove(socks);
            saveToFile();
            return true;
        }
        return false;
    }
    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksWarehouse);
            fileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new FileException("Ошибка сохранения файла");
        }
    }

    private void readFromFile() {
        try {
            String json = fileService.readFromFile();
            socksWarehouse = new ObjectMapper().readValue(json, new TypeReference<Map<Socks, Long>>() {
            });
        } catch (JsonProcessingException e) {
            throw new FileException("Ошибка чтения файла");
        }
    }
    @Override
    public Path getSocksMap() throws IOException {
        Path path = fileService.createTempFile("Socks");
        for (Map.Entry<Socks, Long> socks: socksWarehouse.entrySet()) {
            try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.append("Цвет: " + socks.getKey().getColor() + ". ");
                writer.append("\n");
                writer.append("Размер: " + socks.getKey().getSize());
                writer.append("\n");
                writer.append("Содержание хлопка: " + socks.getKey().getCottonPart() + "%");
                writer.append("\n");
                writer.append("Количество на складе: " + socks.getValue() + " пар");
            }
        }
        return path;
    }
}