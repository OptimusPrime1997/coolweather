package com.coolweather.app.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {

	public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				HttpURLConnection connection = null;
				InputStream in = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.setDoOutput(false);
					connection.setUseCaches(false);
					connection.setConnectTimeout(30000);
					connection.setReadTimeout(30000);
					/* optional request header */
//					connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
					/* optional request header */
					connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					connection.setRequestProperty("Accept-Encoding", "identity");
					connection.setRequestProperty("Accept-Language","en,zh-CN;q=0.8,zh;q=0.6");
					connection.setRequestProperty("Host","www.weather.com.cn");
//					 connection.setRequestProperty("Content-Length", "36");
//					connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
					/* for Get request */
					connection.setRequestMethod("GET");
					int statusCode = connection.getResponseCode();
					Log.d("cool-b", "statusCode:" + statusCode);
					if (statusCode == 200) {
						in = connection.getInputStream();
						if (in != null) {
							Log.d("cool-b", "inputStream:" + in.getClass().getName());
						}
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						StringBuilder response = new StringBuilder();
						String line;
						Log.d("cool-b", "before read webpage");
						while ((line = reader.readLine()) != null) {
							Log.d("cool-b", "line:" + line);
							response.append(line);
						}
						Log.d("cool-b", "address:" + address + "info:" + response);
						if (listener != null) {
							// 回调onFinish方法
							listener.onFinish(response.toString());
						}
					} else {
						Log.d("cool-b", "statusCode!=200:" + statusCode);
					}
				} catch (Exception e) {
					if (listener != null) {
						// 回调onError方法
						Log.d("cool-b", "failed:" + e.getStackTrace());
						listener.onError(e);
					}
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();

	}
}
