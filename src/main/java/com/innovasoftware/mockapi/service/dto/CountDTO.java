package com.innovasoftware.mockapi.service.dto;

public class CountDTO {

    private int count;

    public CountDTO() {
    }

    public CountDTO(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "CountDTO{" +
            "count=" + count +
            '}';
    }
}
