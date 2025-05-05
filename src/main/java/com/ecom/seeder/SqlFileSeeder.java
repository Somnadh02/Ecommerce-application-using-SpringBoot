package com.ecom.seeder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.ecom.repository.ProductRepository;

@Component
public class SqlFileSeeder implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            // Load SQL from classpath resource
            ClassPathResource resource = new ClassPathResource("sample_data.sql");
            try (InputStream inputStream = resource.getInputStream()) {
                String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                try (Connection conn = dataSource.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    System.out.println("✅ Sample data seeded from file.");
                }
            }
        } else {
            System.out.println("ℹ️ Products already exist — skipping seeding.");
        }
    }
}
