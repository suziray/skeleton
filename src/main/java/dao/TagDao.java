package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public List<ReceiptsRecord> find(String tag) {

        Result res = dsl.select(RECEIPTS.ID).from(RECEIPTS
                .innerJoin(TAGS)
                .on(RECEIPTS.ID.equal(TAGS.RID)))
                .where(TAGS.TAG.equal(tag))
                .fetch();

        List<ReceiptsRecord> list = dsl
                .selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.in(res))
                .fetch();

        return list;
    }

    public String update(int rid, String tag) {

        //SELECT all records from TAGS where tag = tag
        //It is a list of records
        //because there might be multiple rids associate with a tag

        //check if receipt exist
        List<ReceiptsRecord> list = dsl
                .selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.equal(rid))
                .fetch();

        if(list.isEmpty()) return "RID_NOT_EXIST";


        //if rid exist, find tags
        List<TagsRecord> lis = dsl
                .selectFrom(TAGS)
                .where(TAGS.RID.equal(rid))
                .fetch();

        //no tags associate with this rid
        //insert directly
        if(lis.isEmpty())  {
            TagsRecord tagsRecord = dsl.insertInto(TAGS, TAGS.RID, TAGS.TAG)
                    .values(rid, tag)
                    .returning(TAGS.ID)
                    .fetchOne();

            checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");
            return "Success";
        }

        //if there are tags, get tags
        for(TagsRecord tr : lis) {
            //if found the tag exist
            if(tr.getTag().equals(tag)) {
                //delete it (untag)
                dsl.deleteFrom(TAGS).where(TAGS.TAG.equal(tag).and(TAGS.RID.equal(rid))).execute();
                return "Delete";
            }
        }

        //there are tags but not duplicate
        TagsRecord tagsRecord = dsl.insertInto(TAGS, TAGS.RID, TAGS.TAG)
                .values(rid, tag)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");
        return "Success";
    }

    public List<TagsRecord> getAllTags() {
        return dsl.selectFrom(TAGS).fetch();
    }
}
