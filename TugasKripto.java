     
// : c14:TugasKripto.java
// Demonstration of File dialog boxes.
// From 'Thinking in Java, 3rd ed.' (c) Bruce Eckel 2002
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

import java.math.BigInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import org.apache.commons.codec.binary.Base64;

public class TugasKripto extends JFrame {
  private JTextField filenamePlain = new JTextField(), dirPlain = new JTextField(), filenameKey = new JTextField(), dirKey = new JTextField(), plainTitle = new JTextField(), keyTitle = new JTextField();

  private JButton plain = new JButton("Plain"),key = new JButton("Key"), enkrip = new JButton("Enkrip"),dekrip = new JButton("Dekrip");
  
  public File fileKey, filePlain;
  
  public String initialKey, initialPlain, initialCipher;
  
  final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

  public TugasKripto() {
    JPanel p = new JPanel();
    plain.addActionListener(new PlainGet());
    p.add(plain);
    
	key.addActionListener(new KeyGet());
    p.add(key);
	
	enkrip.addActionListener(new Enkrip());
    p.add(enkrip);
    
	dekrip.addActionListener(new Dekrip());
    p.add(dekrip);
    
	Container cp = getContentPane();
    cp.add(p, BorderLayout.NORTH);
    dirPlain.setEditable(false);
    filenamePlain.setEditable(false);
	dirKey.setEditable(false);
    filenameKey.setEditable(false);
	plainTitle.setEditable(false);
	keyTitle.setEditable(false);
	plainTitle.setText("Plain: (Cipher jika dekrip)");
	keyTitle.setText("Key:(harus berisi teks hexadecimal)");
    p = new JPanel();
    p.setLayout(new GridLayout(3, 4));
   
	//Menampilkan judul dari plaintext dan key, judul ini akan menjadi penanda kolom plaintext dan key
	p.add(plainTitle);
   	p.add(keyTitle);

	//Menampilkan direktori dari plaintext dan key yang dipilih
	p.add(dirPlain);
    p.add(dirKey);

	//Menampilkan nama file dari plaintext dan key yang dipilih
	p.add(filenamePlain);	
	p.add(filenameKey);
	
    cp.add(p, BorderLayout.SOUTH);
  }

  //PlainGet disini kalau mau dekrip, isinya jadinya file yang isinya ciphertext
  //Kalau mau enkrip, berarti yang diisi plaintext
  class PlainGet implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser c = new JFileChooser();
      // Demonstrate "Open" dialog:
      int rVal = c.showOpenDialog(TugasKripto.this);
      if (rVal == JFileChooser.APPROVE_OPTION) {
        filenamePlain.setText(c.getSelectedFile().getName());
        dirPlain.setText(c.getCurrentDirectory().toString());
		filePlain = c.getSelectedFile();
      }
      if (rVal == JFileChooser.CANCEL_OPTION) {
        filenamePlain.setText("You pressed cancel");
        dirPlain.setText("");
      }
    }
  }
  class KeyGet implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser c = new JFileChooser();
      // Demonstrate "Open" dialog:
      int rVal = c.showOpenDialog(TugasKripto.this);
      if (rVal == JFileChooser.APPROVE_OPTION) {
        filenameKey.setText(c.getSelectedFile().getName());
        dirKey.setText(c.getCurrentDirectory().toString());
		fileKey = c.getSelectedFile();
      }
      if (rVal == JFileChooser.CANCEL_OPTION) {
        filenameKey.setText("You pressed cancel");
        dirKey.setText("");
      }
    }
  }
  
  class Enkrip implements ActionListener {
    public void actionPerformed(ActionEvent e){
		if(filePlain==null){
			filenamePlain.setText("Isi plaintext terlebih dahulu");
			dirPlain.setText("");
		}
		if(fileKey==null){
			filenameKey.setText("Isi key terlebih dahulu");
			dirKey.setText("");
		}
		
		else{
			try{
				initialPlainAndKey();
			}
			catch(IOException ex){}	
		}
    }
  }
  
  class Dekrip implements ActionListener {
    public void actionPerformed(ActionEvent e) {
		if(filePlain==null){
			filenamePlain.setText("Isi plaintext terlebih dahulu");
			dirPlain.setText("");
		}
		if(fileKey==null){
			filenameKey.setText("Isi key terlebih dahulu");
			dirKey.setText("");
		}
		else{
			try{
				//Output dari hasil enkrip akan masuk ke file, isi file tersebut adalah hex, tinggal langsung baca isi dari file 
				Scanner inCipher = new Scanner(filePlain);
				initialCipher = inCipher.nextLine();
				
				//Karena isi key-nya sudah langsung hex, maka tinggal langsung baca isi dari file
				Scanner inKey = new Scanner(fileKey);
				initialKey = inKey.nextLine();
				
				//Mulai dekripsi
				BackwardAES.dekripsi(initialCipher,initialKey);
			}
			catch(IOException ex){}
		}
    }
  }

  public static void main(String[] args) throws FileNotFoundException {
	String s = "65";
	byte value = Byte.valueOf(s);
	System.out.println(value);
    run(new TugasKripto(), 640, 140);
  }
  
  public void initialPlainAndKey() throws FileNotFoundException,IOException{
		//Convert binary image file to String data
            
		FileInputStream textPlain = new FileInputStream(filePlain);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = textPlain.read(b)) != -1) {
           bos.write(b, 0, bytesRead);
        }
        byte[] ba = bos.toByteArray();
        initialPlain = bytesToHex(ba);
		//System.out.println(initialPlain);
		textPlain.close();
		
		//Karena isi key-nya sudah langsung hex, maka tinggal langsung baca isi dari file
		Scanner inKey = new Scanner(fileKey);
		initialKey = inKey.nextLine();
		
		//Mulai enkripsi
		ForwardAES.enkripsi(initialPlain,initialKey);
  }
  

  public static void run(JFrame frame, int width, int height) {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(width, height);
    frame.setVisible(true);
  }
  
  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
    return new String(hexChars);
  }
} 