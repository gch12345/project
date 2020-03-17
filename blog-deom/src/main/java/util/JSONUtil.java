package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mouble.Article;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONUtil {
    public static String serialize(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.DATE_PATTERN));
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败" + obj, e);
        }
    }

    public static <T> T deserialize(InputStream is, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.DATE_PATTERN));
        try {
            return objectMapper.readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setUserId(3);
        article.setContent("ww");
        article.setCreateTime(new Date());
        String json = serialize(article);
        System.out.println(json);
    }
}
