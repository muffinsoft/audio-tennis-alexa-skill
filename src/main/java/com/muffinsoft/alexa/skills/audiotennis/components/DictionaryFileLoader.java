package com.muffinsoft.alexa.skills.audiotennis.components;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DictionaryFileLoader {

    public Set<String> upload(String filename) throws IOException {

        Path path = definePathToFile(filename);

        return createStreamFromFilePath(path);
    }

    private Path definePathToFile(String filename) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile()).toPath();
    }

    private Set<String> createStreamFromFilePath(Path path) throws IOException {
        return Files.lines(path).collect(Collectors.toSet());
    }
}
