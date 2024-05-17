package co.istad.idata.generator;

import co.istad.idata.domains.dynamic_schema.DynamicColumn;
import com.squareup.javapoet.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

public class Generator {

    private static final String DOMAIN_PACKAGE = "co.istad.idata.generated_classes.domain";
    private static final String CONTROLLER_PACKAGE = "co.istad.idata.generated_classes.controller";
    private static final String SERVICE_PACKAGE = "co.istad.idata.generated_classes.service";
    private static final String REPOSITORY_PACKAGE = "co.istad.idata.generated_classes.repository";

    public void generateEntity(String packageName, String className,String schemaName, List<DynamicColumn> columnList) throws IOException {

        // Entity generating
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Entity.class)
                .addAnnotation(Setter.class)
                .addAnnotation(Getter.class)
                .addAnnotation(NoArgsConstructor.class)
                .addAnnotation(AnnotationSpec.builder(Table.class)
                        .addMember("name", "$S", className.toLowerCase()+"s")
                        .addMember("schema", "$S", schemaName)
                        .build());

        for (DynamicColumn column : columnList) {
            FieldSpec field;
            if (column.getColumnName().equals("id")) {
                field = FieldSpec.builder(Long.class, column.getColumnName(), Modifier.PRIVATE)
                        .addAnnotation(Id.class)
                        .addAnnotation(AnnotationSpec.builder(GeneratedValue.class)
                                .addMember("strategy", "$T.IDENTITY", GenerationType.class)
                                .build())
                        .addAnnotation(AnnotationSpec.builder(Column.class)
                                .addMember("name", "$S", column.getColumnName())
                                .build())
                        .build();
            } else {
                field = FieldSpec.builder(getJavaType(column.getType()), column.getColumnName(), Modifier.PRIVATE)
                        .addAnnotation(AnnotationSpec.builder(Column.class)
                                .addMember("name", "$S", column.getColumnName())
                                .build())
                        .build();
            }
            classBuilder.addField(field);

        }
        JavaFile javaFile = JavaFile.builder(DOMAIN_PACKAGE, classBuilder.build()).build();
        javaFile.writeTo(Paths.get("./src/main/java/"));

        // Repository generating
        TypeSpec repository = TypeSpec.interfaceBuilder(className + "Repository")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(JpaRepository.class),
                        ClassName.get(DOMAIN_PACKAGE, className),
                        ClassName.get(Long.class)
                ))
                .build();

        JavaFile javaFile1 = JavaFile.builder(REPOSITORY_PACKAGE, repository)
                .build();
        javaFile1.writeTo(Paths.get("./src/main/java/"));

        // Service generating
        TypeSpec service = TypeSpec.classBuilder(className + "Service")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Service.class)
                .addAnnotation(RequiredArgsConstructor.class)
                .addField(FieldSpec.builder(ClassName.get(REPOSITORY_PACKAGE, className + "Repository"), "repository", Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addMethod(MethodSpec.methodBuilder("findAll")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(packageName, className)))
                        .addStatement("return repository.findAll()")
                        .build())
                // Add more methods here for CRUD operations
                .addMethod(generateCreateMethod(DOMAIN_PACKAGE, className))
                .addMethod(generateUpdateMethod(DOMAIN_PACKAGE, className))
                .addMethod(generateDeleteMethod(DOMAIN_PACKAGE, className))
                .addMethod(generateFindByIdMethod(DOMAIN_PACKAGE, className))
                .build();

        JavaFile javaFile2 = JavaFile.builder(SERVICE_PACKAGE, service).build();
        javaFile2.writeTo(Paths.get("./src/main/java/"));

        // Controller Generating
        ClassName serviceClassName = ClassName.get(SERVICE_PACKAGE, className + "Service");
        ClassName entityClassName = ClassName.get(DOMAIN_PACKAGE, className);
        ParameterSpec entityParameter = ParameterSpec.builder(entityClassName, "entity")
                .addAnnotation(RequestBody.class)
                .build();
        ParameterSpec idParameter = ParameterSpec.builder(Long.class, "id")
                .addAnnotation(PathVariable.class)
                .build();

        TypeSpec controller = TypeSpec.classBuilder(className + "Controller")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(RestController.class)
                .addAnnotation(RequiredArgsConstructor.class)
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$S", "/api/v1/" + toTableName(className) + "s")
                        .build())
                .addField(FieldSpec.builder(serviceClassName, "service", Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addMethod(MethodSpec.methodBuilder("getAll" + className + "s")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(GetMapping.class)
                        .returns(ParameterizedTypeName.get(ClassName.get(List.class), entityClassName))
                        .addStatement("return service.findAll()")
                        .build())
                .addMethod(MethodSpec.methodBuilder("create" + className)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(PostMapping.class)
                        .returns(entityClassName)
                        .addParameter(entityParameter)
                        .addStatement("return service.create$L(entity)", className)
                        .build())
                .addMethod(MethodSpec.methodBuilder("update" + className)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(PutMapping.class)
                        .returns(entityClassName)
                        .addParameter(entityParameter)
                        .addStatement("return service.update$L(entity)", className)
                        .build())
                .addMethod(MethodSpec.methodBuilder("delete" + className + "ById")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(AnnotationSpec.builder(DeleteMapping.class)
                                .addMember("value", "$S", "/{id}")
                                .build())
                        .addParameter(idParameter)
                        .addStatement("service.delete$LById(id)", className)
                        .build())
                .build();
        JavaFile javaFile3 = JavaFile.builder(CONTROLLER_PACKAGE, controller).build();
        javaFile3.writeTo(Paths.get("./src/main/java/"));

    }

    private String toTableName(String className) {
        return className.toLowerCase();
    }

    private TypeName getJavaType(String sqlType) {
        switch (sqlType.toLowerCase()) {
            case "int":
            case "integer":
                return ClassName.get(Integer.class);
            case "bigint":
                return ClassName.get(Long.class);
            case "smallint":
                return ClassName.get(Short.class);
            case "tinyint":
                return ClassName.get(Byte.class);
            case "bit":
            case "boolean":
                return ClassName.get(Boolean.class);
            case "decimal":
            case "numeric":
                return ClassName.get(java.math.BigDecimal.class);
            case "real":
                return ClassName.get(Float.class);
            case "float":
            case "double":
                return ClassName.get(Double.class);
            case "char":
            case "varchar":
            case "text":
                return ClassName.get(String.class);
            case "date":
                return ClassName.get(java.sql.Date.class);
            case "time":
                return ClassName.get(java.sql.Time.class);
            case "timestamp":
            case "datetime":
                return ClassName.get(java.sql.Timestamp.class);
            case "blob":
                return ClassName.get(byte[].class);
            case "clob":
                return ClassName.get(java.sql.Clob.class);
            default:
                throw new IllegalArgumentException("Unknown type: " + sqlType);
        }
    }
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    private MethodSpec generateCreateMethod(String packageName, String className) {
        // Add implementation for create method
        return MethodSpec.methodBuilder("create" + className)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(packageName, className), "entity")
                .returns(ClassName.get(packageName, className))
                .addStatement("return repository.save(entity)")
                .build();
    }

    private MethodSpec generateUpdateMethod(String packageName, String className) {
        // Add implementation for update method
        return MethodSpec.methodBuilder("update" + className)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(packageName, className), "entity")
                .returns(ClassName.get(packageName, className))
                .addStatement("return repository.save(entity)")
                .build();
    }

    private MethodSpec generateDeleteMethod(String packageName, String className) {
        ParameterSpec idParameter = ParameterSpec.builder(Long.class, "id")
                .build();

        return MethodSpec.methodBuilder("delete" + className + "ById")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(idParameter)
                .addStatement("repository.deleteById(id)")
                .build();
    }

    private MethodSpec generateFindByIdMethod(String packageName, String className) {
        // Add implementation for findById method
        return MethodSpec.methodBuilder("findById" + className)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Long.class, "id")
                .returns(ClassName.get(packageName, className))
                .addStatement("return repository.findById(id).orElse(null)")
                .build();
    }
}
