import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog {
    private JTextField tfEmail;
    private JTextField tfName;
    private JTextField tfPhone;
    private JTextField tfAddres;
    private JTextField pfPassword;
    private JTextField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel RegisterPanel;

    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(RegisterPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddres.getText();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

   if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
       JOptionPane.showMessageDialog(this,"please enter all fields","Try again", JOptionPane.ERROR_MESSAGE);
       return;
   }

   if(!password.equals(confirmPassword)){
        JOptionPane.showMessageDialog(this,"password arent equals","Try again", JOptionPane.ERROR_MESSAGE);
        return;
   }

   user = addUserToDataBase(name,email,phone,address,password);
    if(user != null){
        dispose();
    }else{
        JOptionPane.showMessageDialog(this,"Failed to register new User","Try again",JOptionPane.ERROR_MESSAGE);
    }
    }
    public User user;
    private User addUserToDataBase(String name, String email, String phone, String address, String password) {
        User user = null;
        final String DB_URL = "";
        final String USERNAME="root";
        final String PASSWORD="";

        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            //Connected succesfully

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name,email,phone,address,password)"+"VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(1,email);
            preparedStatement.setString(1,phone);
            preparedStatement.setString(1,address);
            preparedStatement.setString(1,password);
            int addedRows= preparedStatement.executeUpdate();
            if(addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }
            stmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();

        }
        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm(null);
        User user = myForm.user;
        if(user != null){
            System.out.println("Successful registration of: " + user.name);
        }else{
            System.out.println("Registration cancelled");
        }
    }
}