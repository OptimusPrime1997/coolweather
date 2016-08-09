package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
	// 解析和處理服務器發回的省級數據
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {

		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(response)) {
			Log.d("cool-a", "province response:" + response);
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// 將解析的數據存儲到province表
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}

		}
		return false;

	}

	// 解析處理服務器返回的市級數據
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			Log.d("cool-a", "city response:" + response);
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					Log.d("cool-a", "city response:" + c);
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					// 將解析的數據存儲到city表
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	// 解析處理服務器返回的县級數據
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					// 將解析的數據存儲到county表
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储在本地
	 * 
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			Log.w("cool-b", "handleWeatherResponse:"+response);
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
			;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将服务器的所有天气信息存储到SharePreferences文件中
	 * 
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2,
			String weatherDesp, String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		Log.w("cool-b", "save Weather info:cityName:" + cityName + "weatherCode:" + weatherCode + "temp1:" + temp1
				+ "temp2:" + temp2 + "weatherDesp:" + weatherDesp + "publish_time:" + publishTime);
		editor.commit();
	}

}
