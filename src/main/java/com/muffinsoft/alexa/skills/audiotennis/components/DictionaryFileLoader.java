package com.muffinsoft.alexa.skills.audiotennis.components;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DictionaryFileLoader {

    public Set<String> uploadCollection(String filename) throws IOException {

        Path path = definePathToFile(filename);

        return createSetFromFilePath(path);
    }

    public Map<String, String> uploadMap(String filename) throws IOException {

        Path path = definePathToFile(filename);

        return createMapFromFilePath(path);
    }

    private Path definePathToFile(String filename) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile()).toPath();
    }

    private Set<String> createSetFromFilePath(Path path) throws IOException {
        return Files.lines(path).collect(Collectors.toSet());
    }

    private Map<String, String> createMapFromFilePath(Path path) throws IOException {
        Map<String, String> result = new HashMap<>();
        Files.lines(path).forEach(line -> {
            String[] split = line.split(",");
            String key = split[0];
            String value = split[1];
            result.put(key, value);
        });
        return result;
    }
}
