package com.adiops.apigateway.common.utils;

public class KeyIDUtils {

	
	
	public static String generateLPKey(long count)
	{
		return "LP"+String.format("%5d", ++count);
	}
	
	public static String generateKey(String prefix,long count)
	{
		return prefix+String.format("%3d", ++count);
	}
	
	
}
