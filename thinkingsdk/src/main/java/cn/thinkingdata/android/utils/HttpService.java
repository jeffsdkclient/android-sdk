/*
 * Copyright (C) 2022 ThinkingData
 */

package cn.thinkingdata.android.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * HttpService发送数据.
 */
public class HttpService implements RemoteService {
    private static final  String TAG = "ThinkingAnalytics.HttpService";

    @Override
    public String performRequest(String endpointUrl, String params,
                                 boolean debug, SSLSocketFactory socketFactory,
                                 Map<String, String> extraHeaders)
            throws ServiceUnavailableException, IOException {
        InputStream in = null;
        OutputStream out = null;
        BufferedOutputStream bout = null;
        BufferedReader br = null;
        HttpURLConnection connection = null;


        try {
            final URL url = new URL(endpointUrl);
            connection = (HttpURLConnection) url.openConnection();
            if (null != socketFactory && connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
            }

            if (null != params) {
                String query;

                connection.setConnectTimeout(15000);
                connection.setReadTimeout(20000);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                if (debug) {
                    query = params;
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    connection.setUseCaches(false);
                    connection.setRequestProperty("charset", "utf-8");
                } else {
                    connection.setRequestProperty("Content-Type", "text/plain");
                    try {
                        query = encodeData(params);
                    } catch (IOException e) {
                        throw new InvalidParameterException(e.getMessage());
                    }
                }
                if (extraHeaders != null) {
                    for (Map.Entry<String, String> entry : extraHeaders.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }

                connection.setFixedLengthStreamingMode(query.getBytes("UTF-8").length);
                out = connection.getOutputStream();
                bout = new BufferedOutputStream(out);
                bout.write(query.getBytes("UTF-8"));

                bout.flush();
                bout.close();
                bout = null;
                out.close();
                out = null;

                int responseCode = connection.getResponseCode();
                TDLog.d(TAG, "ret_code:" + responseCode);
                if (responseCode == 200) {
                    in = connection.getInputStream();
                    br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder buffer = new StringBuilder();
                    String str;
                    while ((str = br.readLine()) != null) {
                        buffer.append(str);
                    }
                    in.close();
                    br.close();
                    return buffer.toString();
                } else {
                    throw new ServiceUnavailableException(
                            "Service unavailable with response code: " + responseCode);
                }
            } else {
                throw new InvalidParameterException("Content is null");
            }

        } finally {
            if (null != bout) {
                try {
                    bout.close();
                } catch (final IOException e) {
                    //ignored
                }
            }

            if (null != out) {
                try {
                    out.close();
                } catch (final IOException e) {
                    //ignored
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (final IOException ignored) {
                    //ignored
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (final IOException ignored) {
                    //ignored
                }
            }
            if (null != connection) {
                connection.disconnect();
            }
        }
    }

    private String encodeData(final String rawMessage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(rawMessage.getBytes().length);
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(rawMessage.getBytes());
        gos.close();
        byte[] compressed = os.toByteArray();
        os.close();
        return new String(Base64Coder.encode(compressed));
    }
}
