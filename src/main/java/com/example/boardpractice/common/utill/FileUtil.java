package com.example.boardpractice.common.utill;

import java.util.Arrays;
import java.util.List;

public class FileUtil {
    private static final List<String> IMAGE_MIME_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png"
    );
    public static boolean isImageFile(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        return IMAGE_MIME_TYPES.contains(mimeType.toLowerCase());
    }
}
