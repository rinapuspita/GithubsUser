package com.jiayou.githubsuser.model;

import java.util.ArrayList;

public class Item {
    String totalCount, incomplete_results;
    ArrayList<ItemResponse> items;

    public Item(String totalCount, String incomplete_results, ArrayList<ItemResponse> items) {
        this.totalCount = totalCount;
        this.incomplete_results = incomplete_results;
        this.items = items;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(String incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public ArrayList<ItemResponse> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemResponse> items) {
        this.items = items;
    }
}
