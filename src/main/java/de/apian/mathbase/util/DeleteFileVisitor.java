package de.apian.mathbase.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Löschender Dateibesucher.
 *
 * @author Nikolas Kirschstein
 * @version 1.0
 * @since 1.0
 */
public class DeleteFileVisitor extends SimpleFileVisitor<Path> {
    /**
     * Aktionen beim Besuch einer Datei.
     *
     * @param file Dateipfad
     * @param attr Dateiattribute
     * @return Dateibesuchsergebnis
     * @throws IOException falls Das Löschen der Datei fehlschlägt
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
    }

    /**
     * Aktionen vor dem Besuch eines Verzeichnisses.
     *
     * @param dir  Verzeichnispfad
     * @param e Eventuell aufgetretene Ausnahme
     * @return Verzeichnisbesuchsergebnis
     * @throws IOException falls das Löschen des Verzeichnisses fehlschlägt
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
    }
}