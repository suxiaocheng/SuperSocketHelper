import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail implements Runnable {
	String text;
	String filename;

	SendEmail(String _text, String _filename) {
		super();
		text = _text;
		filename = _filename;
	}

	public void run() {
		Properties props = new Properties();
		// ����debug����
		props.setProperty("mail.debug", "false");
		// ���ͷ�������Ҫ�����֤
		props.setProperty("mail.smtp.auth", "true");
		// �����ʼ�������������
		props.setProperty("mail.host", "smtp.163.com");
		// �����ʼ�Э������
		props.setProperty("mail.transport.protocol", "smtp");

		// ���û�����Ϣ
		Session session = Session.getInstance(props);

		// �����ʼ�����
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("simulator_test@163.com"));

			msg.setSubject("JavaMail test");

			// create and fill the first message part
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(text);

			// create the second message part
			MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
				mbp2.attachFile(filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("File attatch excepiton is happened");
				filename = null;
				e.printStackTrace();
			}

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			if(filename != null){
				mp.addBodyPart(mbp2);
			}

			// add the Multipart to the message
			msg.setContent(mp);

			// set the Date: header
			msg.setSentDate(new Date());

			Transport transport = session.getTransport();
			// �����ʼ�������
			transport.connect("simulator_test", "simulator123");
			// �����ʼ�
			transport.sendMessage(msg, new Address[] { new InternetAddress("suxiaocheng2010@hotmail.com") });
			// �ر�����
			transport.close();
			
			System.out.println("SendMail " + text + " successfully");

		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			System.out.println("Exception is happened");
			e1.printStackTrace();
		}
		System.out.println("SendMail thread is quit");
	}
}
