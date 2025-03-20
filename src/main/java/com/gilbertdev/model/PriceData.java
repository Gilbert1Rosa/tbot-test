package com.gilbertdev.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceData {
    @JsonProperty("e")
    private String event;

    @JsonProperty("s")
    private String pair;

    @JsonProperty("p")
    private String priceChange;

    @JsonProperty("P")
    private String priceChangePercent;

    public PriceData() {}

    public PriceData(String event, String priceChange, String pair, String priceChangePercent) {
        this.event = event;
        this.priceChange = priceChange;
        this.pair = pair;
        this.priceChangePercent = priceChangePercent;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(String priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }
}
