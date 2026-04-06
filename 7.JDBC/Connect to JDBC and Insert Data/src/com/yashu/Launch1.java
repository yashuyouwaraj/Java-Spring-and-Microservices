
import java.sql.DriverManager;
import java.sql.SQLException;

public class Launch1 {
    public static void main(String[] args) {
        try {
            Class.forName("com.yashu.Demo");
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println("Driver registered successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Requested class was not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Driver registration failed.");
            e.printStackTrace();
        }
    }
}

class Demo {
    static {
        System.out.println("Static block");
    }

    {
        System.out.println("Instance block ==> Non Static");
    }
}
