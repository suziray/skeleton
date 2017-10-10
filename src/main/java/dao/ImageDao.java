package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.ImagesRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.IMAGES;

public class ImageDao {
    DSLContext dsl;

    public ImageDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public List<ReceiptsRecord> find(int id) {

        Result res = dsl.select(RECEIPTS.ID).from(RECEIPTS
                .innerJoin(IMAGES)
                .on(RECEIPTS.ID.equal(IMAGES.RID)))
                .where(IMAGES.ID.equal(id))
                .fetch();

        List<ReceiptsRecord> list = dsl
                .selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.in(res))
                .fetch();

        return list;
    }

    public String update(int rid, String imageString) {

        //SELECT all records from IMAGESTRING where imageString = imageString
        //It is a list of records
        //because there might be multiple rids associate with a imageString

        //check if receipt exist
        List<ReceiptsRecord> list = dsl
                .selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.equal(rid))
                .fetch();

        if(list.isEmpty()) return "RID_NOT_EXIST";


        //if rid exist, find imageString
        List<ImagesRecord> lis = dsl
                .selectFrom(IMAGES)
                .where(IMAGES.RID.equal(rid))
                .fetch();

        //no imageString associate with this rid
        //insert directly
        if(lis.isEmpty())  {
            ImagesRecord imagesRecord = dsl.insertInto(IMAGES, IMAGES.RID, IMAGES.IMAGESTRING)
                    .values(rid, imageString)
                    .returning(IMAGES.ID)
                    .fetchOne();

            checkState(imagesRecord != null && imagesRecord.getId() != null, "Insert failed");
            return "Success";
        }

        //if there are imageString, get imageString
        for(ImagesRecord tr : lis) {
            //if found the imageString exist
            if(tr.getImagestring().equals(imageString)) {
                //delete it (unimageString)
                dsl.deleteFrom(IMAGES).where(IMAGES.IMAGESTRING.equal(imageString).and(IMAGES.RID.equal(rid))).execute();
                return "Delete";
            }
        }

        //there are imageString but not duplicate
        ImagesRecord imagesRecord = dsl.insertInto(IMAGES, IMAGES.RID, IMAGES.IMAGESTRING)
                .values(rid, imageString)
                .returning(IMAGES.ID)
                .fetchOne();

        checkState(imagesRecord != null && imagesRecord.getId() != null, "Insert failed");
        return "Success";
    }

    public List<ImagesRecord> getAllImages() {
        return dsl.selectFrom(IMAGES).fetch();
    }
}
