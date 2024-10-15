import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner = scanner;
    }

    public long open_account(String email){

        if(!account_exist(email)){

            String account_open_query = "INSERT INTO account(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?,?)";

            scanner.nextLine();

            System.out.println("Full name");
            String fullname = scanner.nextLine();
            System.out.println("email");
            String Email = scanner.nextLine();
            System.out.println("Initial Balance");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Security Pin");
            String security_pin = scanner.nextLine();
             try {
                 long account_number = generateAccountNumber();

                 PreparedStatement preparedStatement = connection.prepareStatement(account_open_query);
                 preparedStatement.setLong(1,account_number);
                 preparedStatement.setString(2,fullname);
                 preparedStatement.setString(3,Email);
                 preparedStatement.setDouble(4,balance);
                 preparedStatement.setString(5,security_pin);

                 int rowsaffected = preparedStatement.executeUpdate();

                 if(rowsaffected>0){
                     return account_number;
                 }else{
                     throw new RuntimeException("account creation failed");

                 }
             }catch(SQLException e){
                 System.out.println(e.getMessage());
             }

        }
        throw new RuntimeException("Account Already Exist");

    }

    public long getAccount_number(String email) {
        String query = "SELECT account_number from account WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }




    private long generateAccountNumber() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from account ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            } else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }







    public boolean account_exist(String email){
        String query = "SELECT account_number from account WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }
}
