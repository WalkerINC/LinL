package com.LingualeoExport;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Reciever {
    public static Logger log = Logger.getLogger(InputReader.class.getName());
    String email;
    String password;
    //private String responseString;
    private String actualCookie;

    Reciever(String email, String password, Map<String, Integer> wordsMap){
        this.email = email;
        this.password = password;

        String responseString = init();
        actualCookie = cookieStringer(responseString);

        sendWordsFromMap(wordsMap);


    }
    private String init(){
        CloseableHttpClient httpClient = HttpClients.createMinimal();
        HttpGet httpGet = new HttpGet("http://lingualeo.com/api/login?email="+email+"&password="+password);
        addHeaders(httpGet, true, null);
        CloseableHttpResponse httpResponse = null;
        // Обработка ошибок... господеиисусе...
        try {
            httpResponse = httpClient.execute(httpGet);
            return httpResponse.toString();
        } catch (IOException e) {
            log.info("Something gone wrong. Failed executing in httpResponse");
            e.printStackTrace();
        }finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }else{
                    log.info("httpResponse was null.");
                }
                httpClient.close();
            } catch (IOException e) {
                log.info("Something gone wrong. httpClient or httpResponse throw IOException.");
                e.printStackTrace();
            }

        }
        return null;
    }

    /*
    Доподлено неизвестно, как ведёт себя сервер лигвалео на той стороне. На пустой гет он даже куку не пришлёт. Потому имитирую браузер, как умею.
    Параметры на входе:
    httpGet - понятно,
    логический isThisAuth - "авторизация ли это". В зависимости от этого, определяется заголовок Host в запросе:
        api.lingualeo.com - в случае уже пройденной авторизации
        lingualeo.com - ещё не пройденная авторизация
    cookieString - костыль, в котором передаётся уже готовая строка куков. Это нужно, чтобы добавить её как заголовок. Если это авторизация - передать можно что угодно, т.к. будет пропущено. Потому нуль.
    */
    private void addHeaders(HttpGet httpGet, boolean isThisAuth, String cookieString){
        if (!isThisAuth){
            httpGet.addHeader("Host", "api.lingualeo.com");
        }else {
            httpGet.addHeader("Host", "lingualeo.com");
        }
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate");
        httpGet.addHeader("DNT", "1");
        if (!isThisAuth){
            httpGet.addHeader("Cookie", cookieString);
        }
        httpGet.addHeader("Connection", "keep-alive");
    }
    // Сборка параметра для заголовка кук
    private String cookieStringer(String responseString){
        StringBuilder stringBuilder = new StringBuilder("servid=");
        stringBuilder.append(extract(responseString, "servid=", ";"));
        stringBuilder.append("; remember=");
        stringBuilder.append(extract(responseString, "remember=", ";"));
        stringBuilder.append("; AWSELB=");
        stringBuilder.append(extract(responseString, "AWSELB=", ";"));
        stringBuilder.append("; lingualeouid=");
        stringBuilder.append(extract(responseString, "lingualeouid=", ";"));
        stringBuilder.append("; userid=");
        stringBuilder.append(extract(responseString, "userid=", ";"));
        return stringBuilder.toString();
    }
    private String extract(String str, String start, String end){
        int s = str.indexOf("\n\n", 0), e;
        if(s < 0) s = str.indexOf("\r\n\r\n", 0);
        if(s > 0) str = str.substring(0, s);
        s = str.indexOf(start, 0)+start.length();
        if(s < start.length()) return null;
        e = str.indexOf(end, s);
        if(e < 0) e = str.length();
        return (str.substring(s, e).trim());
    }
    private void sendWordsFromMap(Map<String, Integer> wordsMap){
        Set<Map.Entry<String, Integer>> EntrySet = InputReader.wordsMap.entrySet();
        for(Map.Entry<String, Integer> pair : EntrySet){
            // достаём слова
            CloseableHttpClient httpClientForWords = HttpClients.createMinimal();
            HttpGet httpGetForWords = new HttpGet();
            addHeaders(httpGetForWords, false, actualCookie);
            try {
                addword(pair.getKey(), httpClientForWords, httpGetForWords);
            // господеиисусе опять эти обработки исключений
            } catch (URISyntaxException e) {
                log.info("URISyntaxException. May be Syntax-error in a word?");
                e.printStackTrace();
            } catch (IOException e) {
                log.info("IOException.");
                e.printStackTrace();
            } catch (InterruptedException e) {
                log.info("InterruptedException");
                e.printStackTrace();
            }finally {
                try {
                    httpClientForWords.close();
                } catch (IOException e) {
                    log.info("IOException in httpClientForWords.close()");
                    e.printStackTrace();
                }
            }
        }
    }
    protected static void addword(String word, HttpClient httpClient, HttpGet httpGet) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("http://api.lingualeo.com/addword?word="+ word);
        httpGet.setURI(uri);
        HttpResponse response = httpClient.execute(httpGet);
        System.out.println(word + " - " + response.getStatusLine().getStatusCode());
    }
}
