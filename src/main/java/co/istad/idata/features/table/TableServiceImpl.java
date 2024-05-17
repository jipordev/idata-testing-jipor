package co.istad.idata.features.table;

import co.istad.idata.domains.dynamic_schema.DynamicColumn;
import co.istad.idata.domains.dynamic_schema.DynamicSchema;
import co.istad.idata.domains.dynamic_schema.DynamicTable;
import co.istad.idata.domains.auth.User;
import co.istad.idata.features.schema.DynamicSchemaRepository;
import co.istad.idata.features.table.dto.TableRequest;
import co.istad.idata.features.table.dto.TableResponse;
import co.istad.idata.features.table.dto.column.ColumnRequest;
import co.istad.idata.features.table.dto.column.UpdateColumnRequest;
import co.istad.idata.features.user.UserRepository;
import co.istad.idata.generator.DynamicClassLoader;
import co.istad.idata.generator.Generator;
import co.istad.idata.mapper.TableMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final SessionFactory sessionFactory;
    private final TableMapper tableMapper;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final DynamicSchemaRepository dynamicSchemaRepository;
    private final ColumnRepository columnRepository;

    private static final String DOMAIN_PACKAGE = "co.istad.idata.generated_classes.domain";
    private static final String CONTROLLER_PACKAGE = "co.istad.idata.generated_classes.controller";
    private static final String SERVICE_PACKAGE = "co.istad.idata.generated_classes.service";
    private static final String REPOSITORY_PACKAGE = "co.istad.idata.generated_classes.repository";

    Generator generator = new Generator();

    @Override
    public void createTable(TableRequest tableRequest) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Check if the table already exists
            DynamicTable table = tableRepository.findByTableName(tableRequest.tableName());
            if (table != null && tableRequest.tableName().equals(table.getTableName())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Table already exists"
                );
            }

            // Retrieve user information
            User user = userRepository.findById(tableRequest.userId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );

            // Generate schema name based on username and userId
            String schemaName = user.getUsername() + "_" + user.getId();

            // Check if the table exists in the schema
            if (table != null && table.getSchema().getSchemaName().equals(schemaName)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Table already existed in schema: " + schemaName
                );
            }

            if (tableRepository.findByTableNameAndSchema_SchemaName(tableRequest.tableName(), schemaName).isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Table name: [ " + tableRequest.tableName() + " ] is already existed"
                );
            }

            // Ensure column list is not empty
            List<DynamicColumn> columnList = tableMapper.fromColumnRequest(tableRequest.columns());
            if (columnList.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Column list cannot be empty"
                );
            }

            // Create schema if it doesn't exist
            String createSchemaDdl = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
            session.createNativeQuery(createSchemaDdl).executeUpdate();

            // Get table name from request
            String tableName = tableRequest.tableName();

            // Build the DDL for creating the table
            StringBuilder ddl = new StringBuilder("CREATE TABLE ");
            ddl.append(schemaName).append(".").append(tableName).append(" (");

            for (DynamicColumn column : columnList) {
                ddl.append(column.getColumnName()).append(" ").append(column.getType());
                if (column.isPrimaryKey()) {
                    ddl.append(" PRIMARY KEY");
                }
                ddl.append(",");
            }
            ddl.deleteCharAt(ddl.length() - 1); // Remove the last comma
            ddl.append(")");

            // Execute the DDL to create the table
            session.createNativeQuery(ddl.toString()).executeUpdate();

            // Save schema metadata in the repository if it doesn't already exist
            DynamicSchema schema = dynamicSchemaRepository.findBySchemaName(schemaName)
                    .orElseGet(() -> {
                        DynamicSchema newSchema = new DynamicSchema();
                        newSchema.setSchemaName(schemaName);
                        newSchema.setCreatedAt(LocalDateTime.now());
                        newSchema.setOwner(user);
                        return dynamicSchemaRepository.save(newSchema);
                    });

            // Save table metadata in the repository
            DynamicTable newTable = new DynamicTable();
            newTable.setCreatedAt(LocalDateTime.now());
            newTable.setTableName(tableName);
            newTable.setSchema(schema);
            newTable.setOwner(user);
            tableRepository.save(newTable);

            // Save column metadata in the repository
            for (DynamicColumn column : columnList) {
                column.setDynamicTable(newTable);
                columnRepository.save(column);
            }

            String className = toClassName(tableName);

            generator.generateEntity(DOMAIN_PACKAGE, className,schemaName, columnList);
            DynamicClassLoader.compileAndLoad(DOMAIN_PACKAGE + "." + className);

            transaction.commit();
            log.info("Table '{}' created successfully in schema '{}'.", tableName, schemaName);

        } catch (Exception e) {
            log.error("Error creating table: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating table");
        }
    }

    private static String toClassName(String tableName) {
        String[] words = tableName.split("_");
        StringBuilder classNameBuilder = new StringBuilder();
        for (String word : words) {
            classNameBuilder.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase());
        }
        String className = classNameBuilder.toString();
        if (className.endsWith("s")) {
            className = className.substring(0, className.length() - 1);
        }
        return className;
    }

    @Override
    public List<TableResponse> getAll() {
        List<DynamicTable> tables = tableRepository.findAll();
        return tableMapper.toTableResponse(tables);
    }

    @Override
    public void updateColumn(UpdateColumnRequest updateColumnRequest) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Retrieve user information
            User user = userRepository.findById(updateColumnRequest.userId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );

            // Generate schema name based on username and userId
            String schemaName = user.getUsername() + "_" + user.getId();

            // Retrieve the schema entity
            DynamicSchema schema = dynamicSchemaRepository.findBySchemaName(schemaName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schema not found"));

            // Check if the table exists in the schema
            DynamicTable dynamicTable = tableRepository.findByTableNameAndSchema(updateColumnRequest.tableName(), schema)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));

            // Get the old column name and new column details
            String oldColumnName = updateColumnRequest.oldColumnName();
            ColumnRequest newColumnDetails = updateColumnRequest.newColumnDetails();

            // Fetch the existing column to check if it's a primary key
            DynamicColumn existingColumn = dynamicTable.getColumns().stream()
                    .filter(column -> column.getColumnName().equals(oldColumnName))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));

            // Build the DDL for altering the column
            StringBuilder ddl = new StringBuilder("ALTER TABLE ");
            ddl.append(schemaName).append(".").append(dynamicTable.getTableName());

            if (existingColumn.isPrimaryKey() || newColumnDetails.primaryKey()) {
                // Handle primary key updates with extra caution
                if (!existingColumn.isPrimaryKey() && newColumnDetails.primaryKey()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add primary key constraint to existing column.");
                } else if (existingColumn.isPrimaryKey() && !newColumnDetails.primaryKey()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot remove primary key constraint from existing column.");
                }

                // Rename and update type
                ddl.append(" RENAME COLUMN ").append(oldColumnName)
                        .append(" TO ").append(newColumnDetails.columnName()).append("; ");

                ddl.append("ALTER TABLE ").append(schemaName).append(".").append(dynamicTable.getTableName())
                        .append(" ALTER COLUMN ").append(newColumnDetails.columnName())
                        .append(" TYPE ").append(newColumnDetails.type());
            } else {
                // Only rename and update type if not a primary key
                ddl.append(" RENAME COLUMN ").append(oldColumnName)
                        .append(" TO ").append(newColumnDetails.columnName()).append("; ");

                ddl.append("ALTER TABLE ").append(schemaName).append(".").append(dynamicTable.getTableName())
                        .append(" ALTER COLUMN ").append(newColumnDetails.columnName())
                        .append(" TYPE ").append(newColumnDetails.type());
            }

            // Execute the DDL to update the column
            session.createNativeQuery(ddl.toString()).executeUpdate();

            transaction.commit();
            log.info("Column '{}' in table '{}' updated successfully.", oldColumnName, dynamicTable.getTableName());

        } catch (Exception e) {
            log.error("Error updating column: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating column");
        }
    }

}
