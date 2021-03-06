package org.testcontainers.containers;

import org.testcontainers.utility.LicenseAcceptance;

/**
 * @author Stefan Hufschmidt
 */
public class MSSQLServerContainer<SELF extends MSSQLServerContainer<SELF>> extends JdbcDatabaseContainer<SELF> {

    public static final String NAME = "sqlserver";
    public static final String IMAGE = "mcr.microsoft.com/mssql/server";
    public static final String DEFAULT_TAG = "2017-CU12";

    public static final Integer MS_SQL_SERVER_PORT = 1433;
    private String username = "SA";
    private String password = "A_Str0ng_Required_Password";

    private static final int DEFAULT_STARTUP_TIMEOUT_SECONDS = 240;
    private static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 240;

    public MSSQLServerContainer() {
        this(IMAGE + ":" + DEFAULT_TAG);
    }

    public MSSQLServerContainer(final String dockerImageName) {
        super(dockerImageName);
        withStartupTimeoutSeconds(DEFAULT_STARTUP_TIMEOUT_SECONDS);
        withConnectTimeoutSeconds(DEFAULT_CONNECT_TIMEOUT_SECONDS);
    }

    @Override
    protected Integer getLivenessCheckPort() {
        return getMappedPort(MS_SQL_SERVER_PORT);
    }

    @Override
    protected void configure() {
        addExposedPort(MS_SQL_SERVER_PORT);

        LicenseAcceptance.assertLicenseAccepted(this.getDockerImageName());
        addEnv("ACCEPT_EULA", "Y");

        addEnv("SA_PASSWORD", password);
    }

    @Override
    public String getDriverClassName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlserver://" + getContainerIpAddress() + ":" + getMappedPort(MS_SQL_SERVER_PORT);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getTestQueryString() {
        return "SELECT 1";
    }
}
