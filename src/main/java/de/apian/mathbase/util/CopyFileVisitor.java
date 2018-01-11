package de.apian.mathbase.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Kopierender Dateibesucher.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private Path from, to;

    CopyFileVisitor(Path from, Path to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
        Files.createDirectories(to.resolve(from.relativize(dir)));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
        Files.copy(file, to.resolve(from.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
        return FileVisitResult.CONTINUE;
    }
}