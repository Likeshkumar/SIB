package com.ifp.util;
import java.util.regex.Pattern;
public class IPvalidation 
{
	// Program to validate IPv4 address in Java
	
		private static final String IPv4_REGEX =
				"^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";

		private static final Pattern IPv4_PATTERN = Pattern.compile(IPv4_REGEX);

		public static boolean isValidInet4Address(String ip) 
		{

			if (ip == null) {
				return false;
			}

			if (!IPv4_PATTERN.matcher(ip).matches())
				return false;

			String[] parts = ip.split("\\.");

			// verify that each of the four subgroups of IPv4 address is legal
			try {
				for (String segment: parts) {
					// x.0.x.x is accepted but x.01.x.x is not
					if (Integer.parseInt(segment) > 255 ||
								(segment.length() > 1 && segment.startsWith("0"))) {
						return false;
					}
				}
			} catch(NumberFormatException e) {
				return false;
			}

			return true;
		}
}
