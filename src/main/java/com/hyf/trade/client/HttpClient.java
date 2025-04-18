package com.hyf.trade.client;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2021/12/11
 */
public class HttpClient {

    private static final CloseableHttpClient CLIENT = createClient();

    private static final RequestConfig DEFAULT_CONFIG = createConfig();

    // public static final ServiceLoader<RequestCustomizer> requestCustomizers = ServiceLoader.load(RequestCustomizer.class);
    //
    // static {
    //     // eager init 多线程下载时再初始化会导致在 it.hasNext() 时抛出 NoSuchElementException
    //     Iterator<RequestCustomizer> it = requestCustomizers.iterator();
    //     while (it.hasNext()) {
    //         it.next();
    //     }
    // }

    public static CloseableHttpResponse get(String url) throws IOException {
        return get(new HttpGet(url));
    }

    // TODO quic协议支持
    // TODO 反爬头添加
    public static CloseableHttpResponse get(HttpUriRequestBase request) throws IOException {
        request.setConfig(DEFAULT_CONFIG);
        // request.setHeader("Content-Type", "*/*");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
        // Iterator<RequestCustomizer> it = requestCustomizers.iterator();
        // while (it.hasNext()) {
        //     RequestCustomizer customizer = it.next();
        //     request = customizer.customize(request);
        // }
        return CLIENT.execute(request);
    }

    public static String getString(String url) throws IOException {
        return getString(new HttpGet(url));
    }

    // TODO 错误响应码处理情况
    public static String getString(HttpUriRequestBase request) throws IOException {
        try (CloseableHttpResponse response = get(request)) {
            InputStream is = response.getEntity().getContent();
            return readAsString(is);
        }

        // StringBuilder sb = new StringBuilder();
        // sb.append(id).append(" ").append(sl.getStatusCode());
        // InputStream content = response.getEntity().getContent();
        // if (content != null) {
        //     sb.append(" ").append(IOUtils.readAsString(content).replaceAll("\n", ""));
        // }
        // System.out.println(sb.toString());
    }

    public static String postString(String url, String body, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        headers.forEach(httpPost::addHeader);
        httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = post(httpPost)) {
            InputStream is = response.getEntity().getContent();
            return readAsString(is);
        }
    }

    // TODO quic协议支持
    // TODO 反爬头添加
    public static CloseableHttpResponse post(HttpUriRequestBase request) throws IOException {
        request.setConfig(DEFAULT_CONFIG);
        // request.setHeader("Content-Type", "*/*");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
        // Iterator<RequestCustomizer> it = requestCustomizers.iterator();
        // while (it.hasNext()) {
        //     RequestCustomizer customizer = it.next();
        //     request = customizer.customize(request);
        // }
        return CLIENT.execute(request);
    }

    private static CloseableHttpClient createClient() {

        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException("Get SSLContext instance failed");
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, null, null, NoopHostnameVerifier.INSTANCE);

        Registry registry = RegistryBuilder.create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", csf)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(5000); // 最大连接数3000
        connectionManager.setDefaultMaxPerRoute(500); // 路由链接数400

        RequestConfig requestConfig = RequestConfig.custom()
                // .setSocketTimeout(60000)
                // .setConnectTimeout(60000)
                // .setConnectionRequestTimeout(10000)
                .build();

        return HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .evictExpiredConnections()
                // .evictIdleConnections(30, TimeUnit.SECONDS)
                .evictIdleConnections(TimeValue.ofSeconds(30))
                .build();
    }

    private static RequestConfig createConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(Duration.of(5, ChronoUnit.SECONDS)))
                .setResponseTimeout(Timeout.of(Duration.of(120, ChronoUnit.SECONDS)))
                // .setSocketTimeout(30000)
                .build();
    }

    public static String readAsString(InputStream is) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writeTo(is, baos);
            return baos.toString(StandardCharsets.UTF_8.name());
        }
    }

    private static void writeTo(InputStream is, OutputStream os) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);

        int len;
        byte[] bytes = new byte[1024];
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }

        bos.flush();
    }
}
