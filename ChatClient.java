import java.io.*;
import java.net.*;
import javax.swing.*;
import java.lang.Math;

import java.awt.*;
import java.awt.event.*;
import static java.lang.System.out;
import java.util.Random; 
public class  ChatClient extends JFrame implements ActionListener {
    String uname;
    PrintWriter pw;
    BufferedReader br;
    JTextArea  taMessages;
    JTextField tfInput;
    JButton btnSend,btnExit;
    Socket client;
    String st;
    
    private static final int MAX_VERIFICATION_CODE = 100000;
    private static final int MIN_VERIFICATION_CODE = 999999;
    public static final String ACCOUNT_SID =
            "AC9b02d76d09a255617ce1cfaa9f93aff0";
    public static final String AUTH_TOKEN =
            "ea16d77dc7a7fb4711acf264a55afec3";

    
    public ChatClient(String uname,String phone, String servername) throws Exception {
        super(uname);  //title
        this.uname = uname;
        client  = new Socket(servername,9999);
        br = new BufferedReader( new InputStreamReader( client.getInputStream()) ) ;
        pw = new PrintWriter(client.getOutputStream(),true);
        pw.println(uname);
        pw.println(phone);
        buildInterface();
        new MessagesThread().start(); 
    } 
     
    public void buildInterface() {
        btnSend = new JButton("Send");
        btnExit = new JButton("Exit"); 
        taMessages = new JTextArea();
        taMessages.setRows(10);
        taMessages.setColumns(50);
        taMessages.setEditable(false);
        tfInput  = new JTextField(50);
        JScrollPane sp = new JScrollPane(taMessages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel( new FlowLayout());
        bp.add(tfInput);
        bp.add(btnSend);
        bp.add(btnExit); 
        add(bp,"South");
        btnSend.addActionListener(this);
        btnExit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    } 
    
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == btnExit ) {
            pw.println("end");  
            System.exit(0);
        } else {
            pw.println(tfInput.getText());
		}
    }
    
    public static void main(String ... args) {
    
        String name = JOptionPane.showInputDialog(null,"Enter your name :", "Username",
             JOptionPane.PLAIN_MESSAGE);
        String phone = JOptionPane.showInputDialog(null,"Enter your phone number (+79XXXXXXXXX) :", "Phone number",
                JOptionPane.PLAIN_MESSAGE);
        String code = SendMessage(phone);
        String userCode = JOptionPane.showInputDialog(null,"Enter your verification code :", "Verification code",
                JOptionPane.PLAIN_MESSAGE);
        if (code.equals(userCode))
        {
        	String servername = "localhost";  
        	try {
        		new ChatClient( name, phone, servername);
        	} catch(Exception ex) {
        		out.println( "Error --> " + ex.getMessage());
        	}
        }else {
        	JOptionPane.showMessageDialog(null, "Wrong Verification code! "+code +" "+userCode);
        }
        
    } 
    
    class  MessagesThread extends Thread {
        public void run() {
            String line;
            try {
                while(true) {
                    line = br.readLine();
                    taMessages.append(line + "\n");
                } 
            } catch(Exception ex) {}
        }
    }
    static String SendMessage(String number) {
            
        String code = generateVerificationCode();
        
        SMSCSender sd= new SMSCSender("a-v-boris", "jspm562");
        sd.sendSms(number, "Your password: "+code, 1, "", "", 0, "", "");
        return code;
    }
    
    static String generateVerificationCode() {
        Random rand = new Random();
        Integer code = rand.nextInt(MIN_VERIFICATION_CODE
                - MAX_VERIFICATION_CODE + 1) + MAX_VERIFICATION_CODE;
        return code.toString();
    }
}