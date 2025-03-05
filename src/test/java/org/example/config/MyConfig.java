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

    /**
     * Users
    * */
    public static final String USER_LOGIN_FIELD = "user_login";
    public static final String USER_EMAIL_FIELD = "user_email";

    /**
     * Posts
     * */
    public static final String POST_TITLE_FIELD = "post_title";
    public static final String POST_CONTENT_FIELD = "post_content";



    /**
     * Tags
     * */
    public static final String TAG_NAME_FIELD = "name";
    public static final String TAG_SLUG_FIELD = "slug";
}