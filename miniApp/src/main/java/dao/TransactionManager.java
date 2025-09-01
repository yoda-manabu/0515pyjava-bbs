//DBのトランザクション管理（コミット・ロールバック）を担当。

package dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private Connection conn;

    public TransactionManager(Connection conn) {
        this.conn = conn;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
