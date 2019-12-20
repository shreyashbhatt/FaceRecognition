package Application;

import java.sql.*;

public class DB_Connection {
    private Connection conn = null;
    public Connection getConenctionInstance(){
        if(conn == null) return connectdb();
        else return conn;
    }
    public static Connection connectdb()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/face_recognition","root","28021413");
            return conn;
        }
        catch(Exception e)
        {
           e.printStackTrace();
            return null;
        }
    }
}

