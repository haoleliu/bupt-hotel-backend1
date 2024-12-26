package com.haole.bupthotelbackend.service;

import com.haole.bupthotelbackend.model.domain.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalRecordStorage {
    private static GlobalRecordStorage instance;
    private Map<Long, List<Record>> recordMap;

    private GlobalRecordStorage() {
        recordMap = new HashMap<>();
    }

    public static synchronized GlobalRecordStorage getInstance() {
        if (instance == null) {
            instance = new GlobalRecordStorage();
        }
        return instance;
    }

    public void addRecord(Long roomNumber, Record record) {
        recordMap.computeIfAbsent(roomNumber, k -> new ArrayList<>()).add(record);
    }

    public List<Record> getRecords(Long roomNumber) {
        return recordMap.getOrDefault(roomNumber, new ArrayList<>());
    }
}
