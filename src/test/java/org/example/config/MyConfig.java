package org.example.config;

/**
 * Класс для хранения конфигурационных констант приложения.
 * */
public class MyConfig {
    public static final String URL = "http://localhost:8000";
    public static final String URL_POSTS_ENDPOINT = "/index.php?rest_route=/wp/v2/posts";
    public static final String URL_TAGS_ENDPOINT = "/index.php?rest_route=/wp/v2/tags";
    public static final String URL_USERS_ENDPOINT = "/index.php?rest_route=/wp/v2/users";
    public static final String USERNAME = "Firstname.LastName";
    public static final String PASSWORD = "123-Test";
    public static final String TEST_TITLE = "Test Title";
    public static final String TEST_CONTENT = "Test Content";
    public static final String UPDATED_TITLE = "Updated Title";
    public static final String UPDATED_CONTENT = "Updated Content";
    public static final String POST_STATUS = "publish";
    public static final int AUTHOR_ID = 1;
    public static final int NON_EXISTENT_ID = 999;
}