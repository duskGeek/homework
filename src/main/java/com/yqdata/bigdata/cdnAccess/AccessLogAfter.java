package com.yqdata.bigdata.cdnAccess;

public class AccessLogAfter {

    private String accessIp;
    private String proxyIp;
    private int responseTime;
    private String referer;
    private String method;
    private String httpCode;
    private String requestSize;
    private String responseSize;
    private String hitCache;
    private String userAgent;
    private String fileType;
    private String country;
    private String prov;
    private String city;
    private String year;
    private String day;
    private String month;
    private String protocol;
    private String domain;
    private String path;
    private String query;

    public AccessLogAfter(String accessIp, String proxyIp, int responseTime, String referer, String method,
                          String httpCode, String requestSize, String responseSize, String hitCache, String userAgent,
                          String fileType, String country, String prov, String city, String year, String day,
                          String month, String protocol, String domain, String path, String query) {
        this.accessIp = accessIp;
        this.proxyIp = proxyIp;
        this.responseTime = responseTime;
        this.referer = referer;
        this.method = method;
        this.httpCode = httpCode;
        this.requestSize = requestSize;
        this.responseSize = responseSize;
        this.hitCache = hitCache;
        this.userAgent = userAgent;
        this.fileType = fileType;
        this.country = country;
        this.prov = prov;
        this.city = city;
        this.year = year;
        this.day = day;
        this.month = month;
        this.protocol = protocol;
        this.domain = domain;
        this.path = path;
        this.query = query;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return accessIp + '\t' + proxyIp+'\t'+responseTime +'\t'+referer+'\t'+
                method+'\t'+httpCode+'\t'+requestSize+'\t'+responseSize+'\t'+ hitCache+'\t'+ userAgent+'\t'+
                fileType+'\t'+ country+'\t'+ prov+'\t'+ city+'\t'+ year+'\t'+  month+'\t'+day+'\t'+
                protocol+'\t'+ domain+'\t'+ path+'\t'+ query;
    }
}
