package controllers;

import api.ReceiptResponse;
import api.ImageResponse;
import dao.ImageDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.ImagesRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

@Path("/images")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImageController {

    final ImageDao imageString;

    public ImageController(ImageDao imageString) {
        this.imageString = imageString;
    }

    @PUT
    @Path("/{image}")
    public void toggleImage(@PathParam("image") int id, String imageName) {
        //imageName
        String result = imageString.update(id, imageName);
    }

    @GET
    @Path("/{image}")
    public List<ReceiptResponse> getImages(@PathParam("image") int id) {
        List<ReceiptsRecord> receiptRecords = imageString.find(id);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }


    @GET
    public List<ImageResponse> getAllImages() {
        List<ImagesRecord> imageRecords = imageString.getAllImages();
        return imageRecords.stream().map(ImageResponse::new).collect(toList());
    }

}
