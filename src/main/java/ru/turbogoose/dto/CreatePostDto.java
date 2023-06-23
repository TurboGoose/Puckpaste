package ru.turbogoose.dto;

import lombok.Data;
import ru.turbogoose.exceptions.ValidationException;

@Data
public class CreatePostDto {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 1500;
    private static final int MAX_CONTENT_LENGTH = 20000;
    private static final int MAX_LIFETIME_IN_DAYS = 30;
    
    private String title;
    private String description;
    private String content;
    private int expirationTimeInDays;

    public void validate() throws ValidationException {
        StringBuilder sb = new StringBuilder();

        if (getTitle().length() > MAX_TITLE_LENGTH) {
            sb.append(String.format(" Title length must contain no more than %d symbols (got %d);",
                    MAX_TITLE_LENGTH, getTitle().length()));
        }

        if (getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            sb.append(String.format(" Description length must contain no more than %d symbols (got %d);",
                    MAX_DESCRIPTION_LENGTH, getDescription().length()));
        }

        if (getContent().length() > MAX_CONTENT_LENGTH) {
            sb.append(String.format(" Content length must contain no more than %d symbols (got %d);",
                    MAX_CONTENT_LENGTH, getContent().length()));
        }

        if (getExpirationTimeInDays() <= 0) {
            sb.append(String.format(" ExpirationTimeInDays must be positive integer (got %d);",
                    getExpirationTimeInDays()));
        }

        if (getExpirationTimeInDays() > MAX_LIFETIME_IN_DAYS) {
            sb.append(String.format(" ExpirationTimeInDays must not exceed %d (got %d);",
                    MAX_LIFETIME_IN_DAYS, getExpirationTimeInDays()));
        }

        if (!sb.isEmpty()) {
            throw new ValidationException("Post creation request failed validation due to the following reasons:" + sb);
        }
    }
}
