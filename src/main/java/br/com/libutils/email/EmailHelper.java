package br.com.libutils.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
import javax.mail.internet.MimeUtility;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import br.com.libutils.exception.EmailNotSendException;

public class EmailHelper {

    private static final String CHARSET = "UTF-8";

    private static final Character SEPARATOR = ',';

    public enum TipoEmail {
        HTML, TEXTO;
    }

    private static EmailHelperDelegate instance;

    private EmailHelper() {
    }

    synchronized public static void setInstance(EmailHelperDelegate instance) {
        EmailHelper.instance = instance;
    }

    synchronized private static EmailHelperDelegate getInstance() {
        if (EmailHelper.instance == null) {
            EmailHelper.instance = new EmailHelperDelegateImpl();
        }
        return EmailHelper.instance;
    }

    public static void enviarEmail(String destino, String assunto, String mensagem, File file)
            throws EmailNotSendException {
        EmailHelper.getInstance().enviarEmail(destino, assunto, mensagem, file);
    }

    public static void enviarEmail(String[] destinos, String assunto, String mensagem, File file)
            throws EmailNotSendException {
        EmailHelper.getInstance().enviarEmail(destinos, assunto, mensagem, file);
    }

    public static void enviarEmail(String destino, String assunto, String mensagem, TipoEmail tipo, File file)
            throws EmailNotSendException {
        EmailHelper.getInstance().enviarEmail(destino, assunto, mensagem, tipo, file);
    }

    public static void enviarEmail(String[] destinos, String assunto, String mensagem, TipoEmail tipo, File file)
            throws EmailNotSendException {
        EmailHelper.getInstance().enviarEmail(destinos, assunto, mensagem, tipo, file);
    }

    // -- Classes Internas -------------------------------------------------- //

    public static interface EmailHelperDelegate {

        void enviarEmail(String destino, String assunto, String mensagem, File file) throws EmailNotSendException;

        void enviarEmail(String[] destinos, String assunto, String mensagem, File file) throws EmailNotSendException;

        void enviarEmail(String destino, String assunto, String mensagem, TipoEmail tipo, File file)
                throws EmailNotSendException;

        void enviarEmail(String[] destinos, String assunto, String mensagem, TipoEmail tipo, File file)
                throws EmailNotSendException;
    }

    private static class EmailHelperDelegateImpl implements EmailHelperDelegate {

        private static final String JAVAMAIL_SESSION = "java:/Mail";

        private Session getMailResource() throws NamingException {
            InitialContext ic = new InitialContext();
            return (Session) ic.lookup(EmailHelperDelegateImpl.JAVAMAIL_SESSION);
        }

        @Override
        public void enviarEmail(String destino, String assunto, String mensagem, File file)
                throws EmailNotSendException {
            enviarEmail(StringUtils.split(destino, SEPARATOR), assunto, mensagem, file);

        }

        @Override
        public void enviarEmail(String[] destinos, String assunto, String mensagem, File file)
                throws EmailNotSendException {

            enviarEmail(destinos, assunto, mensagem, TipoEmail.TEXTO, file);
        }

        @Override
        public void enviarEmail(String destino, String assunto, String mensagem, TipoEmail tipo, File file)
                throws EmailNotSendException {

            enviarEmail(StringUtils.split(destino, SEPARATOR), assunto, mensagem, tipo, file);

        }

        @Override
        public void enviarEmail(String[] destinos, String assunto, String mensagem, TipoEmail tipo, File file)
                throws EmailNotSendException {

            Session session;

            try {
                session = getMailResource();
            } catch (NamingException e) {
                throw new EmailNotSendException(e);
            }

            mensagem = StringUtils.trimToEmpty(mensagem);

            if (StringUtils.isBlank(mensagem)) {
                return;
            }

            MimeMessage mm = new MimeMessage(session);

            if (tipo == TipoEmail.HTML) {
                try {
                    mm.addHeader("Content-Type", "text/html; charset=\"" + EmailHelper.CHARSET + "\"");
                } catch (MessagingException me) {
                    throw new EmailNotSendException("Erro configurando o header", me);
                }
            }

            assunto = StringUtils.trimToEmpty(assunto);

            try {
                try {
                    mm.setSubject(MimeUtility.encodeText(assunto, EmailHelper.CHARSET, "Q"));
                } catch (UnsupportedEncodingException uee) {
                    mm.setSubject(assunto);
                }
            } catch (MessagingException me) {
                throw new EmailNotSendException("Erro configurando o assunto", me);
            }

            try {
                mm.setSentDate(new Date());
            } catch (MessagingException me) {
                throw new EmailNotSendException("Erro configurando a data de envio", me);
            }

            try {
                Address[] destinatarios = new InternetAddress[destinos.length];
                for (int i = 0; i < destinos.length; i++) {
                    destinatarios[i] = new InternetAddress(destinos[i]);
                }

                mm.setRecipients(Message.RecipientType.TO, destinatarios);
            } catch (MessagingException me) {
                throw new EmailNotSendException("Erro configurando o destinatário", me);
            }

            // anexando arquivo
            try {

                if (file != null) {
                    // Setando mensagem do e-mail
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText(mensagem);

                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    // Setando anexo selecionado
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(file.getName());
                    multipart.addBodyPart(messageBodyPart);

                    // Setando partes na mensagem
                    mm.setContent(multipart);
                } else {
                    if (tipo == TipoEmail.HTML) {
                        mm.setContent(mensagem, "text/html; charset=\"" + EmailHelper.CHARSET + "\"");
                    } else {
                        mm.setText(mensagem, EmailHelper.CHARSET);
                    }
                }
            } catch (MessagingException me) {
                throw new EmailNotSendException("Erro ao anexar arquivo", me);
            }

            try {
                Transport.send(mm);
            } catch (MessagingException me) {
                String msg = "Erro enviando a mensagem";

                if (me != null) {
                    msg += " (" + me.getMessage() + ")";
                }

                throw new EmailNotSendException(msg, me);
            }

        }
    }

}
