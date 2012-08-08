/**
 * This class links to our online server, verifies and downloads the updated file (if any) for the product
 * @author Vaarnan Drolia
 */
package core;

/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpClient
{

	private static final String COMPANY_WEBSITE = "http://ec2-122-248-220-35.ap-southeast-1.compute.amazonaws.com";
	private static final String QUICKTODO_DOWNLOAD_LINK = "http://ec2-122-248-220-35.ap-southeast-1.compute.amazonaws.com/QuickToDo.zip";

	/**
	 * Checks if we have the latest version
	 * 
	 * @param version
	 * @return boolean - true if version is not up to date
	 */
	public static boolean isUpdateRequired(String version) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response;
			HttpEntity entity;

			HttpPost httpost = new HttpPost(COMPANY_WEBSITE);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("version", version));
			httpost.setEntity(new UrlEncodedFormEntity(nvps));
			response = httpclient.execute(httpost);

			entity = response.getEntity();
			String data = "", line;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			while ((line = br.readLine()) != null) {
				data += line;
			}

			if (data.startsWith("true")) {
				return true;
			} else {
				return false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	/**
	 * downloads the latest file
	 */
	public static String downloadUpdatedFile() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response;
			HttpEntity entity;

			HttpPost httpost = new HttpPost(QUICKTODO_DOWNLOAD_LINK);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("download_update", "true"));

			response = httpclient.execute(httpost);

			entity = response.getEntity();
			if (entity != null) {
				FileOutputStream fos = new FileOutputStream(
						"QuickToDoUpdate.zip");
				entity.writeTo(fos);
				fos.close();
				return "QuickToDoUpdate.zip";
			}
			return null;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	/**
	 * gets the checksum of the updated file
	 * 
	 * @return String- the checksum
	 */
	public static String getUpdateFileChecksum() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response;
			HttpEntity entity;

			HttpPost httpost = new HttpPost(COMPANY_WEBSITE);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("checksum", ""));
			httpost.setEntity(new UrlEncodedFormEntity(nvps));
			response = httpclient.execute(httpost);

			entity = response.getEntity();
			String data = "", line;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			while ((line = br.readLine()) != null) {
				data += line;
			}
			if (data.equals("")) {
				return null;
			} else {
				return data;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

}