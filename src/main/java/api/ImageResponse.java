package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ImagesRecord;
import static java.lang.System.out;
import java.sql.Time;

/**
 * This is an API Object.  Its purpose is to model the JSON API that we expose.
 * This class is NOT used for storing in the Database.
 *
 * This ReceiptResponse in particular is the model of a Receipt that we expose to users of our API
 *
 * Any properties that you want exposed when this class is translated to JSON must be
 * annotated with {@link JsonProperty}
 */
public class ImageResponse {
    @JsonProperty
    Integer id;

    @JsonProperty
    Integer receiptid;

    @JsonProperty
    String imageString;



    @JsonProperty
    Time created;

    public ImageResponse(ImagesRecord dbRecord) {
        this.id = dbRecord.getId();
        this.receiptid = dbRecord.getRid();
        this.imageString = dbRecord.getImagestring();
        this.created = dbRecord.getUploaded();
    }
}
