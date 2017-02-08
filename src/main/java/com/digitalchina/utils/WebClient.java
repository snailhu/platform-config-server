package com.digitalchina.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class WebClient {

	public static String sendPost(String url, String param) throws Exception {

		PrintWriter out = null;
		BufferedReader in = null;
		URLConnection conn = null;

		try {
			URL realUrl = new URL(url);
			conn = realUrl.openConnection();// 打开和URL之间的连接
            conn.setRequestProperty("accept", "*/*");// 设置通用的请求属性
			conn.setRequestProperty("connection", "Keep-Alive");// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			out = new PrintWriter(conn.getOutputStream()); // 获取URLConnection对象对应的输出流
			out.print(param);// 发送请求参数
			out.flush();// flush输出流的缓冲

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			
			try{if (out != null) {out.close();}}catch(Exception e){e.printStackTrace();}
			try{if (in != null) {in.close();}}catch(Exception e){e.printStackTrace();}

		}

	}

}