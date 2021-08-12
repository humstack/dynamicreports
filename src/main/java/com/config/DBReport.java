package com.config;

import net.sf.dynamicreports.report.exception.DRException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class DBReport {

    private Connection connection;

    public DBReport() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            createTable();
            build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void build() {
        try {
            report()
                    .setTemplate(template())
                    .columns(
                            col.column("Item", "item", type.stringType()),
                            col.column("Quantity", "quantity", type.integerType()),
                            col.column("Unit price", "unitprice", type.bigDecimalType())
                    ).title(cmp.text("Database Datasource"))
                    .pageFooter(cmp.pageXofY())
                    .setDataSource("SELECT * FROM sales", connection)
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE sales (item VARCHAR(50), quantity INTEGER, unitprice DECIMAL)");
        statement.execute("INSERT INTO sales VALUES ('Book', 5, 100)");
    }
}
