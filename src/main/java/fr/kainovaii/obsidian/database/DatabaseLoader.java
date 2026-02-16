package fr.kainovaii.obsidian.database;

import fr.kainovaii.obsidian.core.EnvLoader;
import fr.kainovaii.obsidian.core.Obsidian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseLoader
{
    public final static Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    public static void loadDatabase()
    {
        System.out.println("Loading database");
        EnvLoader env = Obsidian.loadConfigAndEnv();

        String dbType = env.get("DB_TYPE");
        if (dbType == null || dbType.isEmpty()) { dbType = "sqlite"; }

        switch (dbType.toLowerCase())
        {
            case "sqlite":
                String dbPath = env.get("DB_PATH");
                if (dbPath == null || dbPath.isEmpty()) {
                    dbPath = "data.db";
                }
                DB.initSQLite(dbPath, logger);
                break;
            case "mysql":
                String mysqlHost = env.get("DB_HOST");
                String mysqlPort = env.get("DB_PORT");
                DB.initMySQL(
                        mysqlHost != null ? mysqlHost : "localhost",
                        Integer.parseInt(mysqlPort != null ? mysqlPort : "3306"),
                        env.get("DB_NAME"),
                        env.get("DB_USER"),
                        env.get("DB_PASSWORD"),
                        logger
                );
                break;
            case "postgresql":
                String pgHost = env.get("DB_HOST");
                String pgPort = env.get("DB_PORT");
                DB.initPostgreSQL(
                        pgHost != null ? pgHost : "localhost",
                        Integer.parseInt(pgPort != null ? pgPort : "5432"),
                        env.get("DB_NAME"),
                        env.get("DB_USER"),
                        env.get("DB_PASSWORD"),
                        logger
                );
                break;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}
