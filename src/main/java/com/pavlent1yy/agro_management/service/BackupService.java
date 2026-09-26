package com.pavlent1yy.agro_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BackupService {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "jdbc:postgresql://([^:/]+)(?::(\\d+))?/([^?]+)");

    private final DataSource dataSource;

    @Value("${agro.backup.command:pg_dump}")
    private String backupCommand;

    @Value("${agro.backup.directory:backups}")
    private String backupDirectory;

    @Value("${spring.datasource.password:}")
    private String databasePassword;

    public Path createBackup() {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            String user = connection.getMetaData().getUserName();
            Matcher matcher = URL_PATTERN.matcher(url);
            if (!matcher.matches()) {
                throw new IllegalStateException("Не удалось определить параметры подключения PostgreSQL: " + url);
            }

            String host = matcher.group(1);
            String port = matcher.group(2) == null ? "5432" : matcher.group(2);
            String database = matcher.group(3);

            Path directory = Path.of(backupDirectory).toAbsolutePath().normalize();
            Files.createDirectories(directory);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            Path target = directory.resolve("agro-backup-" + timestamp + ".sql");

            ProcessBuilder builder = new ProcessBuilder(
                    backupCommand,
                    "-h", host,
                    "-p", port,
                    "-U", user,
                    "--no-password",
                    "-f", target.toString(),
                    database);
            builder.environment().put("PGPASSWORD", databasePassword == null ? "" : databasePassword);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("pg_dump завершился с кодом " + exitCode
                        + ". Проверьте установку PostgreSQL и доступность команды " + backupCommand);
            }
            return target;
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка подключения к базе данных при создании резервной копии", e);
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка создания файла резервной копии", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Создание резервной копии прервано", e);
        }
    }
}
