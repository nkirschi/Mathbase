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
public class CopyFileVisitor extends SimpleFileVisitor<Path> {
    /**
     * Ur- und Zielpfad.
     *
     * @since 1.0
     */
    private Path from, to;

    /**
     * Konstruktion des Dateibesuchers.
     *
     * @param from Urpfad
     * @param to   Zielpfad
     * @since 1.0
     */
    public CopyFileVisitor(Path from, Path to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Aktionen vor dem Besuch eines Verzeichnisses.
     *
     * @param dir  Verzeichnispfad
     * @param attr Verzeichnisattribute
     * @return Verzeichnisbesuchsergebnis
     * @throws IOException falls das Kopieren des Verzeichnisses fehlschlägt
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
        Files.createDirectories(to.resolve(from.relativize(dir)));
        return FileVisitResult.CONTINUE;
    }

    /**
     * Aktionen beim Besuch einer Datei.
     *
     * @param file Dateipfad
     * @param attr Dateiattribute
     * @return Dateibesuchsergebnis
     * @throws IOException falls das Kopieren der Datei fehlschlägt
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
        Files.copy(file, to.resolve(from.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
        return FileVisitResult.CONTINUE;
    }
}