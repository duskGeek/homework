package com.yqdata.bigdata.cdnAccess;

public class AccessLog {
    private String time;
    private String accessIp;
    private String proxyIp;
    private int responseTime;
    private String referer;
    private String method;
    private String url;
    private String httpCode;
    private String requestSize;
    private String responseSize;
    private String hitCache;
    private String userAgent;
    private String fileType;

    public AccessLog(String time, String accessIp, String proxyIp, int responseTime, String referer, String method,
                     String url, String httpCode, String requestSize, String responseSize, String hitCache,
                     String userAgent, String fileType) {
        this.time = time;
        this.accessIp = accessIp;
        this.proxyIp = proxyIp;
        this.responseTime = responseTime;
        this.referer = referer;
        this.method = method;
        this.url = url;
        this.httpCode = httpCode;
        this.requestSize = requestSize;
        this.responseSize = responseSize;
        this.hitCache = hitCache;
        this.userAgent = userAgent;
        this.fileType = fileType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        time = time;
    }

    public String getAccessIp() {
        return accessIp;
    }

    public void setAccessIp(String accessIp) {
        this.accessIp = accessIp;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }

    public String getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(String requestSize) {
        this.requestSize = requestSize;
    }

    public String getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(String responseSize) {
        this.responseSize = responseSize;
    }

    public String getHitCache() {
        return hitCache;
    }

    public void setHitCache(String hitCache) {
        this.hitCache = hitCache;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    @Override
    public String toString() {
        return time + "\t" +
                accessIp + "\t" +
                proxyIp + "\t" +
                responseTime + "\t" +
                referer + "\t" +
                method + "\t" +
                url + "\t" +
                httpCode + "\t" +
                requestSize + "\t" +
                responseSize + "\t" +
                hitCache + "\t" +
                userAgent + "\t" +
                fileType + "\t" ;
    }
}
