package io.github.qiangyt.common.jpa;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Database;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;

@lombok.extern.slf4j.Slf4j
public class DialectResolverEx implements DialectResolver {

    /**
     *
     */
    private static final long serialVersionUID = 6786967837824426241L;

    @Override
    public Dialect resolveDialect(DialectResolutionInfo resolutionInfo) {

        String dbName = resolutionInfo.getDatabaseName();
        int verMajor = resolutionInfo.getDatabaseMajorVersion();
        int verMinor = resolutionInfo.getDatabaseMinorVersion();

        log.info("databaseName={}, majorVersion={}, minorVersion={}", dbName, verMajor, verMinor);

        if ("MySQL".equals(dbName)) {
            return new MySQLDialect();
        }

        for (Database db : Database.values()) {
            Dialect dialect = db.createDialect(resolutionInfo);// .resolveDialect(dialectResolutionInfo);
            if (dialect != null) {
                return dialect;
            }
        }

        return null;
    }

}
