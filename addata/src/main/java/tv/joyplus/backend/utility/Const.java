package tv.joyplus.backend.utility;

public interface Const {
	
	public static final String OPERATION_TYPE_REQUST = "002";
	public static final String OPERATION_TYPE_IMPRESSION = "003";
	
	public static final String CREATIVE_TYPE_OPEN = "open";
	public static final String CREATIVE_TYPE_IMAGE = "1";
	public static final String CREATIVE_TYPE_VIDEO = "2";
	public static final String CREATIVE_TYPE_RICHMEDIA = "3";
	public static final String CREATIVE_TYPE_ZIP = "4";
	public static final String CREATIVE_TYPE_VIDEOANDZIP = "5";
	
	public static final int RESULT_STATUS_SUCCESS = 1;
	public static final int RESULT_STATUS_FAILE = 2;

	
	public static final String EXCEPTION_BADSQL = 		"00001";
	public static final String EXCEPTION_JSONPARSE = 	"00002";
	public static final String EXCEPTION_UNKOWN_TYPE = 	"00003";
	public static final String EXCEPTION_DATEPARSE = 	"00004";
	public static final String EXCEPTION_NULL_PARAME = 	"00005";
}
