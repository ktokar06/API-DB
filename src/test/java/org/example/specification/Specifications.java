package org.example.specification;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;
import static org.example.config.MyConfig.URL;


/**
 * Класс для настройки спецификаций запросов
 * */
public class Specifications {

    /**
     *
     * Метод для создания спецификации запроса с базовым набором настроек.
     *
     * @return объект типа {@link RequestSpecification}, содержащий базовые настройки для запросов
     *
     * */
    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setRelaxedHTTPSValidation()
                .setContentType(JSON)
                .setAccept(JSON)
                .build();
    }
}