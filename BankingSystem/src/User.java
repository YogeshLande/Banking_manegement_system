import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();

        System.out.println("Full Name");
        String fullname = scanner.nextLine();
        System.out.println("Email");
        String email = scanner.nextLine();
        System.out.println("Password");
        String password = scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User is Already Exits");
            return;

        }

        String query = "INSERT INTO user(full_name,email,password) VALUES(?,?,?)";

        try{

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,fullname);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int rowsaffected = preparedStatement.executeUpdate();

            if(rowsaffected>0){
                System.out.println("User Register Successfull");
            }else{
                System.out.println("User Register Failed");
            }





        }catch(SQLException e){
            System.out.println(e.getMessage());
        }


    }

    public String login(){
        scanner.nextLine();

        System.out.println("Email");
        String email = scanner.nextLine();
        System.out.println("Password");
        String password = scanner.nextLine();

        String query = "SELECT * FROM user WHERE email = ?AND password = ?";

        try{

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return email;
            }else{
                return null;
            }
        }catch(SQLException e ){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public boolean user_exist(String email){
        String query = "SELECT * FROM user WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }



}
