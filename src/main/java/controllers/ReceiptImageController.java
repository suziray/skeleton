package controllers;

import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;

import static java.lang.System.out;

@Path("/image")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = null;
            BigDecimal amount = null;

            // Your Algo Here!!
            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount
            int b = 0;
            int maxY = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            int x1 = 0;
            int x2 = 0;
            int x3 = 0;
            int x4 = 0;
            int y1 = 0;
            int y2 = 0;
            int y3 = 0;
            int y4 = 0;
            for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                // out.printf("Position : %s\n", annotation.getBoundingPoly());
                // out.printf("Text: %s\n", annotation.getDescription());
                if(b > 0) {
                    int x = annotation.getBoundingPoly().getVertices(0).getX();
                    int y = annotation.getBoundingPoly().getVertices(0).getY();
                    String s = annotation.getDescription();
                    if((y < minY || (y == minY && x < minX)) && !isNumeric(s)) {
                        merchantName = s;
                        minY = y;
                        minX = x;
                    }
                    if((y > maxY || (y == maxY && x > maxX)) && isNumeric(s)){
                        if (s.contains("$")) s = s.substring(1, s.length());
                        amount = new BigDecimal(s);
                        maxY = y;
                        maxX = x;
                    }
                } else {
                    x1 = annotation.getBoundingPoly().getVertices(0).getX();
                    y1 = annotation.getBoundingPoly().getVertices(0).getY();
                    x2 = annotation.getBoundingPoly().getVertices(1).getX();
                    y2 = annotation.getBoundingPoly().getVertices(1).getY();
                    x3 = annotation.getBoundingPoly().getVertices(2).getX();
                    y3 = annotation.getBoundingPoly().getVertices(2).getY();
                    x4 = annotation.getBoundingPoly().getVertices(3).getX();
                    y4 = annotation.getBoundingPoly().getVertices(3).getY();
                    ++b;
                }
            }

            //TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
            return new ReceiptSuggestionResponse(merchantName, amount, x1, y1, x2, y2, x3, y3, x4, y4);
        }
    }

    public boolean isNumeric(String s){
        boolean isNum = true;
        int dot = 0;
        int dol = 0;
        for(int i = 0; i < s.length(); ++i) {
            if (Character.isDigit(s.charAt(i))) {
                // do nothing
            } else if (s.charAt(i) == '.') {
                ++dot;
            } else if (s.charAt(i) == '$') {
                ++dol;
            } else {
                isNum = false;
            }
        }
        return isNum && (dot <= 1) && (dol <= 1);
    }
}

