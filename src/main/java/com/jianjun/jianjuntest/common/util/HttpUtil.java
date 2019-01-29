package com.jianjun.jianjuntest.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * HTTP请求相关处理工具类
 *
 * @author Ready
 * @date 2014年11月1日
 */
public class HttpUtil {

	/**
	 * 获取本地的IP地址。如果能够获取到外网IP，则返回外网IP；否则返回内网IP<br>
	 * <b>注意</b>：暂不支持IPv6地址
	 *
	 * @return
	 * @throws SocketException
	 */
	public static String getLocalIP() throws SocketException {
		String ip = null;// 外网IP(或内网IP)
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ia = null;
		boolean notFound = true;
		while (notFound && netInterfaces.hasMoreElements()) {
			NetworkInterface ni = netInterfaces.nextElement();
			if (!ni.isUp() || ni.isLoopback()) {
				// 如果该网络接口 未启用 或 是本地回环接口，则忽略掉
				continue;
			}
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ia = address.nextElement();
				if ((ia instanceof Inet4Address) && !ia.isLoopbackAddress()) { // 仅处理IPv4地址，并且是非回环地址
					if (!ia.isSiteLocalAddress()) { // 内网IP
						ip = ia.getHostAddress(); // 外网IP
						notFound = false;
						break;
					}
				}
			}
		}
		if (StringUtils.isEmpty(ip)) {
			try {
				ia = Inet4Address.getLocalHost();
			} catch (UnknownHostException e) {
				throw new IllegalStateException(e);
			}
			ip = ia.getHostAddress();
		}
		return ip;
	}

	/**
	 * 获取指定网络地址所对应的网络接口硬件地址(一般是Mac地址)
	 *
	 * @param inetAddress
	 * @return
	 * @throws SocketException
	 */
	public static String getMacAddress(InetAddress inetAddress) throws SocketException {
		byte[] macArray = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
		if (macArray == null) {
			return null;
		}
		StringBuilder mac = new StringBuilder();
		final char[] buf = "0123456789ABCDEF".toCharArray();
		for (int i = 0; i < macArray.length; i++) {
			if (i > 0) {
				mac.append('-');
			}
			mac.append(buf[macArray[i] >> 4 & 0xF]);
			mac.append(buf[macArray[i] & 0xF]);
		}
		return mac.toString();
	}

	/**
	 * 获取本地网络地址所对应的网络接口硬件地址(一般是Mac地址)
	 *
	 * @return
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getLocalMacAddress() throws SocketException, UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		if (ip.isLoopbackAddress()) {
			boolean notFound = true;
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			while (notFound && nis.hasMoreElements()) {
				NetworkInterface ni = nis.nextElement();
				if (ni.isUp() && !ni.isLoopback()) {
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = ips.nextElement();
						if (ip instanceof Inet4Address) {
							notFound = false;
							break;
						}
					}
				}
			}
		}
		return getMacAddress(ip);
	}

	/**
	 * 发起一个常规的HTTP请求，并设置相应的HTTP请求头
	 *
	 * @param request
	 * @param headers
	 * @return
	 */
	public static HttpResponse doHTTP(HttpUriRequest request, Map<String, String> headers) throws IllegalStateException {
		HttpClient client = HttpClients.createDefault();
		appendHeaders(request, headers);
		try {
			return client.execute(request);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 读取HttpResponse的响应内容，并转换为字符串进行输出
	 *
	 * @param response
	 * @return
	 */
	public static String reponseToString(HttpResponse response) throws IllegalStateException {
		HttpEntity entity = response.getEntity();
		try {
			return EntityUtils.toString(entity, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 发起一个常规的HTTP GET请求，并设置相应的HTTP请求头
	 *
	 * @param url
	 * @param headers
	 * @return
	 */
	public static HttpResponse doGET(String url, Map<String, String> headers) {
		return doHTTP(new HttpGet(url), headers);
	}

	/**
	 * 发起一个常规的HTTP GET请求，设置相应的HTTP请求头，并返回字符串形式的响应内容
	 *
	 * @param url
	 * @param headers
	 */
	public static String doGet(String url, Map<String, String> headers) throws IllegalStateException {
		return reponseToString(doHTTP(new HttpGet(url), headers));
	}

	/**
	 * 发起一个常规的HTTP POST请求，设置相应的HTTP请求头，并返回对应的HttpResponse响应对象
	 *
	 * @param url
	 * @param paramEntity
	 * @param headers
	 * @return
	 */
	public static HttpResponse doPOST(String url, HttpEntity paramEntity, Map<String, String> headers) throws IllegalStateException {
		HttpPost post = new HttpPost(url);
		if (paramEntity != null) {
			post.setEntity(paramEntity);
		}
		return doHTTP(post, headers);
	}

	/**
	 * 发起一个常规的HTTP POST请求，设置相应的HTTP请求头，并返回对应的HttpResponse响应对象
	 *
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static HttpResponse doPOST(String url, String params, Map<String, String> headers, Charset charset) throws IllegalStateException {
		return doPOST(url, toEntityFromString(params, null, null), headers);
	}

	/**
	 * 将字符串转为 HttpEntity
	 *
	 * @param params
	 * @param contentType
	 * @param charset
	 */
	public static StringEntity toEntityFromString(String params, String contentType, Charset charset) {
		if (StringUtils.isNotEmpty(params)) {
			StringEntity entity = new StringEntity(params, charset == null ? StandardCharsets.UTF_8 : charset);
			entity.setContentType(StringUtils.isEmpty(contentType) ? "application/x-www-form-urlencoded" : contentType);
			return entity;
		}
		return null;
	}

	/**
	 * 将Map集合转为 HttpEntity
	 *
	 * @param paramMap
	 * @param charset
	 */
	public static UrlEncodedFormEntity toEntityFromMap(Map<String, String> paramMap, Charset charset) {
		if (paramMap != null && paramMap.size() > 0) {
			List<NameValuePair> list = new ArrayList<NameValuePair>(paramMap.size());
			for (Entry<String, String> entry : paramMap.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			return new UrlEncodedFormEntity(list, charset == null ? StandardCharsets.UTF_8 : charset);
		}
		return null;
	}

	/**
	 * 发起一个常规的HTTP POST请求，设置相应的HTTP请求头，并返回字符串形式的响应内容
	 *
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String doPost(String url, String params, Map<String, String> headers, String contentType, Charset charset) throws IllegalStateException {
		return reponseToString(doPOST(url, toEntityFromString(params, contentType, charset), headers));
	}

	/**
	 * 向请求对象 HttpUriRequest 中追加多个请求头
	 *
	 * @param request
	 * @param headers
	 */
	protected static void appendHeaders(HttpUriRequest request, Map<String, String> headers) {
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 指示当前请求是否来自微信内置浏览器
	 */
	public static boolean fromWechat(final HttpServletRequest request) {
		return fromWechat(request.getHeader("User-Agent"));
	}

	/**
	 * 指示当前用户代理是否来自微信内置浏览器
	 */
	public static boolean fromWechat(final String userAgent) {
		return StringUtils.isNotEmpty(userAgent) && userAgent.contains("MicroMessenger");
	}
}