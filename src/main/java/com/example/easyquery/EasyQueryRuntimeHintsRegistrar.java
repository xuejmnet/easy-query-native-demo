package com.example.easyquery;

import com.easy.query.core.api.SQLClientApiFactory;
import com.easy.query.core.api.dynamic.executor.query.WhereObjectQueryExecutor;
import com.easy.query.core.api.dynamic.executor.sort.ObjectSortQueryExecutor;
import com.easy.query.core.basic.api.cte.CteTableNamedProvider;
import com.easy.query.core.basic.api.database.DatabaseCodeFirst;
import com.easy.query.core.basic.entity.EntityMappingRule;
import com.easy.query.core.basic.extension.formater.SQLParameterPrintFormat;
import com.easy.query.core.basic.extension.listener.JdbcExecutorListener;
import com.easy.query.core.basic.extension.print.JdbcSQLPrinter;
import com.easy.query.core.basic.extension.track.TrackManager;
import com.easy.query.core.basic.jdbc.conn.ConnectionManager;
import com.easy.query.core.basic.jdbc.executor.EntityExpressionExecutor;
import com.easy.query.core.basic.jdbc.types.JdbcTypeHandlerManager;
import com.easy.query.core.basic.pagination.EasyPageResultProvider;
import com.easy.query.core.basic.thread.ShardingExecutorService;
import com.easy.query.core.common.MapColumnNameChecker;
import com.easy.query.core.common.bean.DefaultFastBean;
import com.easy.query.core.configuration.EasyQueryOption;
import com.easy.query.core.configuration.QueryConfiguration;
import com.easy.query.core.configuration.bean.PropertyDescriptorMatcher;
import com.easy.query.core.configuration.column2mapkey.Column2MapKeyConversion;
import com.easy.query.core.configuration.dialect.DefaultSQLKeyword;
import com.easy.query.core.configuration.nameconversion.MapKeyNameConversion;
import com.easy.query.core.context.DefaultEasyQueryRuntimeContext;
import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.datasource.DataSourceManager;
import com.easy.query.core.datasource.DataSourceUnitFactory;
import com.easy.query.core.exception.AssertExceptionFactory;
import com.easy.query.core.expression.builder.core.ValueFilterFactory;
import com.easy.query.core.expression.include.IncludeProcessorFactory;
import com.easy.query.core.expression.parser.factory.SQLExpressionInvokeFactory;
import com.easy.query.core.expression.segment.factory.SQLSegmentFactory;
import com.easy.query.core.expression.sql.builder.factory.ExpressionBuilderFactory;
import com.easy.query.core.expression.sql.expression.factory.ExpressionFactory;
import com.easy.query.core.expression.sql.include.IncludeParserEngine;
import com.easy.query.core.expression.sql.include.IncludeProvider;
import com.easy.query.core.expression.sql.include.RelationNullValueValidator;
import com.easy.query.core.expression.sql.include.relation.RelationValueColumnMetadataFactory;
import com.easy.query.core.expression.sql.include.relation.RelationValueFactory;
import com.easy.query.core.extension.casewhen.SQLCaseWhenBuilderFactory;
import com.easy.query.core.func.SQLFunc;
import com.easy.query.core.inject.ServiceProvider;
import com.easy.query.core.job.EasyTimeJobManager;
import com.easy.query.core.metadata.EntityMetadataManager;
import com.easy.query.core.migration.MigrationsSQLGenerator;
import com.easy.query.core.sharding.DefaultEasyQueryDataSource;
import com.easy.query.core.sharding.EasyQueryDataSource;
import com.easy.query.core.sharding.comparer.ShardingComparer;
import com.easy.query.core.sharding.manager.ShardingQueryCountManager;
import com.easy.query.core.sharding.router.manager.DataSourceRouteManager;
import com.easy.query.core.sharding.router.manager.TableRouteManager;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.*;

public class EasyQueryRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    // 注册多个包
    private static final List<String> PACKAGES_TO_SCAN = List.of(
            "com.example.easyquery.entity",
            "com.example.easyquery.dto",
            "com.example.easyquery.entity.proxy"
    );

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        for (String basePackage : PACKAGES_TO_SCAN) {
            Set<Class<?>> classes = scanPackage(basePackage, classLoader);
            for (Class<?> clazz : classes) {
                hints.reflection().registerType(clazz, builder -> builder.withMembers().withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
            }
        }

//        hints.reflection().registerType(TypeReference.of("com.easy.query.sql.starter.logging.Slf4jImpl"), builder -> builder.withConstructor(Arrays.asList(TypeReference.of(String.class)), ExecutableMode.INVOKE).withMembers());
//        hints.reflection().registerType(TypeReference.of("com.easy.query.core.api.client.DefaultEasyQueryClient"), builder -> builder.withConstructor(Arrays.asList(TypeReference.of(QueryRuntimeContext.class)), ExecutableMode.INVOKE).withMembers());
//
//
//
//
//        hints.reflection().registerType(
//                TypeReference.of(DefaultEasyQueryRuntimeContext.class),
//                builder -> builder.withConstructor(Arrays.asList(
//                        TypeReference.of(ServiceProvider.class),
//                        TypeReference.of(EasyQueryDataSource.class),
//                        TypeReference.of(QueryConfiguration.class),
//                        TypeReference.of(EntityMetadataManager.class),
//                        TypeReference.of(SQLExpressionInvokeFactory.class),
//                        TypeReference.of(ConnectionManager.class),
//                        TypeReference.of(EntityExpressionExecutor.class),
//                        TypeReference.of(JdbcTypeHandlerManager.class),
//                        // TypeReference.of(SQLApiFactory.class), // 注释的构造参数，如需使用请取消注释
//                        TypeReference.of(ExpressionBuilderFactory.class),
//                        TypeReference.of(TrackManager.class),
//                        TypeReference.of(EasyPageResultProvider.class),
//                        TypeReference.of(ShardingExecutorService.class),
//                        TypeReference.of(ExpressionFactory.class),
//                        TypeReference.of(TableRouteManager.class),
//                        TypeReference.of(DataSourceRouteManager.class),
//                        TypeReference.of(ShardingComparer.class),
//                        TypeReference.of(ShardingQueryCountManager.class),
//                        TypeReference.of(DataSourceUnitFactory.class),
//                        TypeReference.of(SQLSegmentFactory.class),
//                        TypeReference.of(SQLClientApiFactory.class),
//                        TypeReference.of(DataSourceManager.class),
//                        TypeReference.of(EasyTimeJobManager.class),
//                        TypeReference.of(IncludeProcessorFactory.class),
//                        TypeReference.of(IncludeParserEngine.class),
//                        TypeReference.of(WhereObjectQueryExecutor.class),
//                        TypeReference.of(ObjectSortQueryExecutor.class),
//                        TypeReference.of(JdbcExecutorListener.class),
//                        TypeReference.of(AssertExceptionFactory.class),
//                        TypeReference.of(SQLParameterPrintFormat.class),
//                        TypeReference.of(SQLFunc.class),
//                        TypeReference.of(Column2MapKeyConversion.class),
//                        TypeReference.of(JdbcSQLPrinter.class),
//                        TypeReference.of(RelationValueFactory.class),
//                        TypeReference.of(RelationValueColumnMetadataFactory.class),
//                        TypeReference.of(MapColumnNameChecker.class),
//                        TypeReference.of(PropertyDescriptorMatcher.class),
//                        TypeReference.of(ValueFilterFactory.class),
//                        TypeReference.of(EntityMappingRule.class),
//                        TypeReference.of(MigrationsSQLGenerator.class),
//                        TypeReference.of(CteTableNamedProvider.class),
//                        TypeReference.of(MapKeyNameConversion.class),
//                        TypeReference.of(DatabaseCodeFirst.class),
//                        TypeReference.of(IncludeProvider.class),
//                        TypeReference.of(RelationNullValueValidator.class),
//                        TypeReference.of(SQLCaseWhenBuilderFactory.class)
//                ), ExecutableMode.INVOKE).withMembers()
//        );
//
//        hints.reflection().registerType(
//                TypeReference.of(DefaultEasyQueryDataSource.class),
//                builder -> builder
//                        .withConstructor(
//                                List.of(
//                                        TypeReference.of(EasyQueryOption.class),
//                                        TypeReference.of(DataSourceManager.class)
//                                ),
//                                ExecutableMode.INVOKE
//                        )
//                        .withMembers()
//        );
//
//        hints.reflection().registerType(
//                TypeReference.of("com.easy.query.core.datasource.DefaultDataSourceManager"),
//                builder -> builder
//                        .withConstructor(
//                                List.of(
//                                        TypeReference.of("com.easy.query.core.configuration.EasyQueryOption"),
//                                        TypeReference.of("javax.sql.DataSource"),
//                                        TypeReference.of("com.easy.query.core.datasource.DataSourceUnitFactory")
//                                ),
//                                ExecutableMode.INVOKE
//                        )
//                        .withMembers()
//        );
//
//        hints.reflection().registerType(
//                TypeReference.of("com.easy.query.sql.starter.conn.SpringDataSourceUnitFactory"),
//                builder -> builder
//                        .withConstructor(
//                                List.of(
//                                        TypeReference.of("com.easy.query.core.configuration.EasyQueryOption")
//                                ),
//                                ExecutableMode.INVOKE
//                        )
//                        .withMembers()
//        );
//
//        hints.reflection().registerType(
//                TypeReference.of("com.easy.query.core.configuration.QueryConfiguration"),
//                builder -> builder
//                        .withConstructor(
//                                List.of(
//                                        TypeReference.of("com.easy.query.core.configuration.EasyQueryOption"),
//                                        TypeReference.of("com.easy.query.core.configuration.dialect.SQLKeyword"),
//                                        TypeReference.of("com.easy.query.core.configuration.nameconversion.NameConversion"),
//                                        TypeReference.of("com.easy.query.core.job.EasyTimeJobManager")
//                                ),
//                                ExecutableMode.INVOKE
//                        )
//                        .withMembers()
//        );
//
//        hints.reflection().registerType(DefaultSQLKeyword.class, builder -> {
//            builder.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE);
//        });




    }

    private Set<Class<?>> scanPackage(String basePackage, ClassLoader classLoader) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            String path = "classpath*:" + basePackage.replace('.', '/') + "/**/*.class";
            var resolver = new PathMatchingResourcePatternResolver(classLoader);
            var readerFactory = new CachingMetadataReaderFactory();

            for (var resource : resolver.getResources(path)) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    ClassMetadata metadata = reader.getClassMetadata();
                    Class<?> clazz = Class.forName(metadata.getClassName(), false, classLoader);
                    classes.add(clazz);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to scan classes in package: " + basePackage, e);
        }

        return classes;
    }
}
