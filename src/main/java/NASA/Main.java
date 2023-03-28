package NASA;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;


public class Main {
    public static final String REMOTE_SERVICE_URI =
            "https://api.nasa.gov/planetary/apod?api_key=LODvSpy6xQ14g9IT0NDvbGpOnmvBEvwbobHMjdFZ";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()

                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        CloseableHttpResponse response = httpClient.execute(new HttpGet(REMOTE_SERVICE_URI));

        NasaImage nasa = mapper.readValue(response.getEntity().getContent(), NasaImage.class); // сохранили в java-объект


        CloseableHttpResponse image = httpClient.execute(new HttpGet(nasa.getUrl())); //делаем еще один запрос по на получение url

        HttpEntity entity = image.getEntity();

        String nameFile = nameFile(nasa.getUrl());


        FileOutputStream file = new FileOutputStream(nameFile);
        if (entity != null) {
            entity.writeTo(file);
            file.close();
        }
    }

    public static String nameFile(String str) {
        return str.substring(str.lastIndexOf("/") + 1);
    }

}
