/**
 * This class is generated by jOOQ
 */
package generated;


import generated.tables.Images;
import generated.tables.Receipts;
import generated.tables.Tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.4"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

	private static final long serialVersionUID = 1969629966;

	/**
	 * The reference instance of <code>public</code>
	 */
	public static final Public PUBLIC = new Public();

	/**
	 * No further instances allowed
	 */
	private Public() {
		super("public");
	}

	@Override
	public final List<Sequence<?>> getSequences() {
		List result = new ArrayList();
		result.addAll(getSequences0());
		return result;
	}

	private final List<Sequence<?>> getSequences0() {
		return Arrays.<Sequence<?>>asList(
			Sequences.SYSTEM_SEQUENCE_2BD0EE30_9B11_4E3C_B3C0_9FBA924CCB2F,
			Sequences.SYSTEM_SEQUENCE_C10872E6_E758_42DD_9563_76316D56E7EB,
			Sequences.SYSTEM_SEQUENCE_F098D05F_021B_4F43_A031_ACB8BA270993);
	}

	@Override
	public final List<Table<?>> getTables() {
		List result = new ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final List<Table<?>> getTables0() {
		return Arrays.<Table<?>>asList(
			Receipts.RECEIPTS,
			Tags.TAGS,
			Images.IMAGES);
	}
}
