package com.rybindev.cloudstorage.util;

import com.rybindev.cloudstorage.dto.Breadcrumb;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PathUtils {

    private static final String DELIMITER = "/";

    public static String extractName(String path) {
        return path.substring(path.lastIndexOf(DELIMITER) + 1);
    }

    public static String replaceName(String path, String newName) {
        int index = path.lastIndexOf(DELIMITER);
        if (index < 0) {
            return newName;
        }
        return path.substring(0, index + 1) + newName;
    }

    public List<Breadcrumb> getBreadcrumbs(String path) {
        ArrayList<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new Breadcrumb("root", ""));

        int index = path.indexOf(DELIMITER);

        while (index > 0) {
            String p = path.substring(0, index);
            breadcrumbs.add(new Breadcrumb(p.substring(p.lastIndexOf(DELIMITER) + 1), p));

            index = path.indexOf(DELIMITER, index + 1);
        }
        if (!path.isBlank()) {
            breadcrumbs.add(new Breadcrumb(path.substring(path.lastIndexOf(DELIMITER) + 1), path));
        }

        return breadcrumbs;

    }

    public boolean isValid(String value) {
        return value.isEmpty() || value.matches("^([^/]+/)*[^/]+$");
    }
}
