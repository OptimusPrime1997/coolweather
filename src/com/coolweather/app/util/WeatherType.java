package com.coolweather.app.util;

public enum WeatherType {
	WEATHERCODE(1,"weatherCode"),
	COUNTYCODE(2,"countyCode");
	private int index;
	private String value;
	private WeatherType(int index,String value){
		this.index=index;
		this.value=value;
	}
}
