package us.gantzfamily.dlv.common.lang;

import java.util.Date;

public class DateUtils {

	public static Date min(final Date date1, final Date date2) {
		
		if( date1 == null && date2 == null ) {
			return null;
		}
		
		if( date1 == null ) {
			return date2;
		}
		
		if( date2 == null ) {
			return date1;
		}

		return date1.getTime() < date2.getTime() ? date1 : date2;
		
	}

	public static Date max(final Date date1, final Date date2) {
		
		if( date1 == null && date2 == null ) {
			return null;
		}
		
		if( date1 == null ) {
			return date2;
		}
		
		if( date2 == null ) {
			return date1;
		}

		return date1.getTime() > date2.getTime() ? date1 : date2;
		
	}

}
