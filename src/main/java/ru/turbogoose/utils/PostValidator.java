package ru.turbogoose.utils;

import ru.turbogoose.dto.CreatePostDto;
import ru.turbogoose.exceptions.ValidationException;

public class PostValidator {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 1500;
    private static final int MAX_CONTENT_LENGTH = 20000;
    private static final int MAX_LIFETIME_IN_DAYS = 30;

    public static void validate(CreatePostDto dto) throws ValidationException {
        StringBuilder sb = new StringBuilder();

        String title = dto.getTitle();
        if (title != null && title.length() > MAX_TITLE_LENGTH) {
            sb.append(String.format(" Title length must contain no more than %d symbols (got %d);",
                    MAX_TITLE_LENGTH, title.length()));
        }

        String description = dto.getDescription();
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            sb.append(String.format(" Description length must contain no more than %d symbols (got %d);",
                    MAX_DESCRIPTION_LENGTH, description.length()));
        }

        String content = dto.getContent();
        if (content == null) {
            sb.append(" Required field 'content' is missed;");
        } else if (content.length() > MAX_CONTENT_LENGTH) {
            sb.append(String.format(" Content length must contain no more than %d symbols (got %d);",
                    MAX_CONTENT_LENGTH, content.length()));
        }

        Integer expiration = dto.getExpirationTimeInDays();
        if (expiration != null) {
            if (expiration <= 0) {
                sb.append(String.format(" ExpirationTimeInDays must be positive integer (got %d);", expiration));
            } else if (expiration > MAX_LIFETIME_IN_DAYS) {
                sb.append(String.format(" ExpirationTimeInDays must not exceed %d (got %d);",
                        MAX_LIFETIME_IN_DAYS, expiration));
            }
        }

        if (!sb.isEmpty()) {
            throw new ValidationException("Post creation request failed validation due to the following reasons:" + sb);
        }
    }
}
