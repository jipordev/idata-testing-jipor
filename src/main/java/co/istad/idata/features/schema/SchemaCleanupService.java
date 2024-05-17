package co.istad.idata.features.schema;

import co.istad.idata.domains.dynamic_schema.DynamicSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchemaCleanupService {

    private final DynamicSchemaRepository userSchemaRepository;
    private final SessionFactory sessionFactory;

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpOldSchemas() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1); // Set threshold as needed
        List<DynamicSchema> oldSchemas = userSchemaRepository.findByCreatedAtBefore(cutoff);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            for (DynamicSchema userSchema : oldSchemas) {
                String dropSchemaDdl = "DROP SCHEMA " + userSchema.getSchemaName() + " CASCADE";
                session.createNativeQuery(dropSchemaDdl).executeUpdate();

                // Remove from repository
                userSchemaRepository.delete(userSchema);
                log.info("Schema '{}' deleted successfully.", userSchema.getSchemaName());
            }

            transaction.commit();
        } catch (Exception e) {
            log.error("Error cleaning up schemas: {}", e.getMessage());
        }
    }
}

