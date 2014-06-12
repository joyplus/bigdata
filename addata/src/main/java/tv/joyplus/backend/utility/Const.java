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
	
	
	public static final String CACHE_DVERTISER_CAMPAIGN_NAME_BY_CAMPAIGNID 	= "_CACHE_DVERTISER_CAMPAIGN_NAME_BY_CAMPAIGNID_";
	public static final String CACHE_AD_UNITS_BYID 							= "_CACHE_AD_UNITS_BYID_";
	public static final String CACHE_PUBLICATIONS_BYID 						= "_CACHE_PUBLICATIONS_BYID_";
	public static final String CACHE_REGIONAL_TARGETING_BYCODE 				= "_CACHE_REGIONAL_TARGETING_BYCODE_";
	public static final String CACHE_DEVICE_DATA_BYNAME 					= "_CACHE_DEVICE_DATA_BYNAME_";
	public static final String CACHE_ZONES_BYID 							= "_CACHE_ZONES_BYID_";
}
