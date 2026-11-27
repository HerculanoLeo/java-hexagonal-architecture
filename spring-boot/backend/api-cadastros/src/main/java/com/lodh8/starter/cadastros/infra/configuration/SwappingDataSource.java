package com.lodh8.starter.cadastros.infra.configuration;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Proxy estável do {@link DataSource}: troca o Hikari por baixo sem
 * {@code @RefreshScope}, para o {@code ContextRefresher} não matar o pool
 * quando só mudam mail/CORS/Keycloak.
 */
final class SwappingDataSource implements DataSource, AutoCloseable {

    private final AtomicReference<HikariDataSource> delegate = new AtomicReference<>();

    SwappingDataSource(HikariDataSource initial) {
        this.delegate.set(initial);
    }

    /**
     * Substitui o pool. O Hikari antigo faz shutdown gracioso (espera
     * conexões em uso); pedidos novos já vão para o pool novo.
     */
    void swap(HikariDataSource next) {
        HikariDataSource previous = delegate.getAndSet(next);
        if (previous != null && previous != next) {
            previous.close();
        }
    }

    private HikariDataSource requireDelegate() throws SQLException {
        HikariDataSource current = delegate.get();
        if (current == null) {
            throw new SQLException("DataSource indisponivel");
        }
        return current;
    }

    /** Compatível com reflexão tipo Hikari ({@code DataSourceBuilder.derivedFrom}). */
    public String getJdbcUrl() {
        HikariDataSource current = delegate.get();
        return current != null ? current.getJdbcUrl() : null;
    }

    public String getUsername() {
        HikariDataSource current = delegate.get();
        return current != null ? current.getUsername() : null;
    }

    public String getPassword() {
        HikariDataSource current = delegate.get();
        return current != null ? current.getPassword() : null;
    }

    public String getDriverClassName() {
        HikariDataSource current = delegate.get();
        return current != null ? current.getDriverClassName() : null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return requireDelegate().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return requireDelegate().getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return requireDelegate().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        requireDelegate().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        requireDelegate().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return requireDelegate().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        try {
            return requireDelegate().getParentLogger();
        } catch (SQLException ex) {
            throw new SQLFeatureNotSupportedException(ex);
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        return requireDelegate().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || requireDelegate().isWrapperFor(iface);
    }

    @Override
    public void close() {
        HikariDataSource current = delegate.getAndSet(null);
        if (current != null) {
            current.close();
        }
    }
}
