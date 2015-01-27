package ca.informi.wagoneer.oo.gameobject;

public class FilterBits {

	public static final short HCB0 = 11;
	public static final short[] HITCH_CATEGORY_BIT = new short[] { (1 << HCB0), (1 << HCB0 + 1), (1 << HCB0 + 2), (1 << HCB0 + 3) };
	public static final short HITCH_FILTER_BITS = (short) (HITCH_CATEGORY_BIT[0] | HITCH_CATEGORY_BIT[1] | HITCH_CATEGORY_BIT[2] | HITCH_CATEGORY_BIT[3]);
	public static final short[] HITCH_MASK_BIT = new short[] { HITCH_CATEGORY_BIT[1], HITCH_CATEGORY_BIT[0], HITCH_CATEGORY_BIT[3],
			HITCH_CATEGORY_BIT[2] };

}
