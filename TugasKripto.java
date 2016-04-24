     
// : c14:TugasKripto.java
// Demonstration of File dialog boxes.
// From 'Thinking in Java, 3rd ed.' (c) Bruce Eckel 2002
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

import java.io.PrintWriter;
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
import javax.swing.JOptionPane;

//import org.apache.commons.codec.binary.Base64;

public class TugasKripto extends JFrame {
  private JTextField filenamePlain = new JTextField(), dirPlain = new JTextField(), filenameKey = new JTextField(), dirKey = new JTextField(), plainTitle = new JTextField(), keyTitle = new JTextField();

  private JButton plain = new JButton("Plain"),key = new JButton("Key"), enkrip = new JButton("Enkrip"),dekrip = new JButton("Dekrip");
  
  public File fileKey, filePlain;
  
  public String initialKey, initialPlain, initialCipher;
  
  private static PrintWriter writer;

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
    }
  }
  
  class Enkrip implements ActionListener {
    public void actionPerformed(ActionEvent e){
		//Peringatan kalau belum mengisi plain/ciphertext atau key
		if(filePlain==null){
			filenamePlain.setText("Isi plaintext terlebih dahulu");
			dirPlain.setText("");
			JOptionPane.showMessageDialog(null, "Isi plaintext terlebih dahulu!");
		}
		if(fileKey==null){
			filenameKey.setText("Isi key terlebih dahulu");
			dirKey.setText("");
			JOptionPane.showMessageDialog(null, "Isi key terlebih dahulu!");
		}
		
		//Bila sudah diisi, menjalankan untuk mengubah file menjadi hex
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
		//Peringatan kalau belum mengisi plain/ciphertext atau key
		if(filePlain==null){
			filenamePlain.setText("Isi plaintext terlebih dahulu");
			dirPlain.setText("");
			JOptionPane.showMessageDialog(null, "Isi plaintext terlebih dahulu!");
		}
		if(fileKey==null){
			filenameKey.setText("Isi key terlebih dahulu");
			JOptionPane.showMessageDialog(null, "Isi key terlebih dahulu!");
			dirKey.setText("");
		}
				
		//Bila sudah diisi, menjalankan untuk mengubah file menjadi hex
		else{
			try{
				
				writer = new PrintWriter("out_dekripsi.txt", "UTF-8");
				
				//Output dari hasil enkrip akan masuk ke file, isi file tersebut adalah hex, tinggal langsung baca isi dari file 
				Scanner inCipher = new Scanner(filePlain);
				initialCipher = inCipher.nextLine();
				
				//Karena isi key-nya sudah langsung hex, maka tinggal langsung baca isi dari file
				Scanner inKey = new Scanner(fileKey);
				initialKey = inKey.nextLine();
				
				//Mulai dekripsi
				String result = BackwardXTS_AES.dekripsiSatuUnit(initialCipher,initialKey);
				System.out.println("Hasil Dekripsi : " +result);
				writer.println(result);
				writer.close();
				JOptionPane.showMessageDialog(null, "Dekripsi berhasil!");
			}
			catch(IOException ex){}
		}
    }
  }

  public static void main(String[] args) throws FileNotFoundException {
    run(new TugasKripto(), 640, 140);
  }
  
  public void initialPlainAndKey() throws FileNotFoundException,IOException{
		
		writer = new PrintWriter("out_enkripsi.txt", "UTF-8");
				
		//Agar dapat menerima berbagai jenis file, pada plaintext ini, isinya diubah terlebih dahulu ke string dengan basis 16
		//Jadi bila berbentuk teks, maka bentuk hexnya tidak akan selalu sama dengan isi filenya		
		FileInputStream textPlain = new FileInputStream(filePlain);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = textPlain.read(b)) != -1) {
           bos.write(b, 0, bytesRead);
        }
        byte[] ba = bos.toByteArray();
		
		//Ubah byte ke hex
        initialPlain = bytesToHex(ba);
		textPlain.close();
		
		//Karena isi key-nya sudah langsung hex, maka tinggal langsung baca isi dari file
		Scanner inKey = new Scanner(fileKey);
		initialKey = inKey.nextLine();
		
		//Mulai enkripsi
		String result = ForwardXTS_AES.enkripsiSatuUnit(initialPlain,initialKey);
		System.out.println("Hasil Enkripsi : " +result);
		writer.println(result);
		writer.close();
		JOptionPane.showMessageDialog(null, "Enkripsi berhasil!");
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
