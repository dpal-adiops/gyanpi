package com.adiops.apigateway.common.utils;

public class UserUtils {

	 public static String getKeyId(Long count) {
		 return "LNR"+String.format("%05d",count);
	 }
	
}
