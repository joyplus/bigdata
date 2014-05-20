package tv.joyplus.backend.utility;

public class CommonUtility {
	
	public static boolean isEmptyString(String str){
		if(str==null || str.trim().length()==0){
			return true;
		}else{
			return false;
		}
	}

}
