package api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Represents the result of an OCR parse
 */
public class ReceiptSuggestionResponse {
    @JsonProperty
    public final String merchantName;

    @JsonProperty
    public final BigDecimal amount;

    @JsonProperty
    public final int x1;

    @JsonProperty
    public final int x2;

    @JsonProperty
    public final int x3;

    @JsonProperty
    public final int x4;

    @JsonProperty
    public final int y1;

    @JsonProperty
    public final int y2;

    @JsonProperty
    public final int y3;

    @JsonProperty
    public final int y4;


    public ReceiptSuggestionResponse(String merchantName, BigDecimal amount, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        this.merchantName = merchantName;
        this.amount = amount;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        this.y4 = y4;
    }
}
