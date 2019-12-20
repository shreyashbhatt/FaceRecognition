package Application;

import java.sql.*;

public class DB_Connection {
    Connection conn = null;
    public static Connection connectdb()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/face_recognition","root","28021413");
            return conn;
        }
        catch(Exception e)
        {
           e.printStackTrace();
            return null;
        }
    }
}

