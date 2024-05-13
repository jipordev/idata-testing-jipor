package co.istad.idata.features.table;

import co.istad.idata.domains.DynamicColumn;
import co.istad.idata.features.table.dto.TableRequest;
import co.istad.idata.mapper.TableMapper;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

//    private final EntityManager sessionFactory;

    private SessionFactory sessionFactory;
    private final TableMapper tableMapper;

    @Override
    public void createTable(TableRequest tableRequest) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Create table schema
            String schemaName = "public";
            String tableName = tableRequest.tableName(); // Get table name from request
            StringBuilder ddl = new StringBuilder("CREATE TABLE ");
            ddl.append(schemaName).append(".").append(tableName).append(" (");


            List<DynamicColumn> columnList = tableMapper.fromColumnRequest(tableRequest.columns());

            for (DynamicColumn column : columnList) {
                ddl.append(column.getColumnName()).append(" ").append(column.getType());
                if (column.isPrimaryKey()) {
                    ddl.append(" PRIMARY KEY");
                }
                ddl.append(",");
            }
            ddl.deleteCharAt(ddl.length() - 1); // Remove the last comma
            ddl.append(")");

            // Execute DDL
            session.createNativeQuery(ddl.toString()).executeUpdate();


            transaction.commit();
            log.info("Table '{}' created successfully.", tableName);
        } catch (Exception e) {
            log.error("Error creating table: {}", e.getMessage());
        }
    }
}
