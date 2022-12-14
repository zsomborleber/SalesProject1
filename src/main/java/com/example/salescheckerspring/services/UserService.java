package com.example.salescheckerspring.services;



import com.example.salescheckerspring.models.Order;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.repos.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final JavaMailSender mailSender;

    private User user;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder
                       ,BCryptPasswordEncoder bCryptPasswordEncoder,
                       JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailSender = mailSender;
    }
    public List<User> findAllUser(){
        return (List<User>) userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByEmail(email);
        return optional.orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
    @Transactional
    public User saveUserReg(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        return userRepository.save(user);
    }
    public void saveUser(User user){
        userRepository.save(user);
    }
    @Transactional
    public boolean verify(String verificationCode){
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        }else {
            user.setEnabled(true);
            user.setVerificationCode(null);
            return true;
        }
    }
    public void sendVerificationEmail(User user,String siteUrl) throws UnsupportedEncodingException, MessagingException {
        String subject = "Regisztráció megerősítése";
        String senderName = "Project of Sale Team";
        String mailContent = "<p>Kedves " + user.getCompanyName() + "!</p>";
        mailContent += "<p>Regisztrációjának megerősítéséhez kérem kattintson a 'Megerősítés'-re.</p>";
        String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
        mailContent += "<h3><a href=\"" + verifyUrl + "\">Megerősítés</a></h3>";
        mailContent += "<p>Köszönettel<br>A Project of Sale csapata! </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("pointofsales2022@gmail.com",senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent,true);
        mailSender.send(message);
    }
    public void sendOrderVerificationEmail(User user) throws UnsupportedEncodingException, MessagingException {
        String subject = "Rendelés visszaigazolás";
        String senderName = "Project of Sale Team";
        String mailContent = "<p>Kedves " + user.getCompanyName() + "!</p>";
        mailContent += "<p>Rendelését sikeresen rögzítettük.</p>";
        mailContent += "<p>Köszönettel<br>A Project of Sale csapata! </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("pointofsales2022@gmail.com",senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent,true);
        mailSender.send(message);
    }

    public void sendOrderCompletedEmail(Order order) throws UnsupportedEncodingException, MessagingException {
        String subject = "Megrendelés teljesítve";
        String senderName = "Project of Sale Team";
        String mailContent = "<p>Kedves " + order.getUser().getCompanyName() + "!</p>";
        mailContent += "<p>Az " +order.getId() + " számú megrendelését melynek végösszege "+order.getSumValue(order.getCartItems()) +"Ft sikeresen teljesítettük,</p>";
        mailContent += "<p>a mai napon kiszállításra kerül</p>";
        mailContent += "<p>A rendelés további részleteit az alábbi linkre kattintva tekintheti meg:</p>";
        mailContent += "<h3><a href=\"http://localhost:8080/orders\">Rendeléseim</a></h3>";
        mailContent += "<p>Köszönettel<br>A Project of Sale csapata! </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("pointofsales2022@gmail.com",senderName);
        helper.setTo(order.getUser().getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent,true);
        mailSender.send(message);
    }

    public boolean isEmailAlreadyInUse(User newuser) {
        List<User> users = findAllUser();

        for (User user : users) {
            if (newuser.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTAXNumberAlreadyInUse(User newuser) {
        List<User> users = findAllUser();

        for (User user : users) {
            if (newuser.getTaxNumber().equals(user.getTaxNumber())) {
                return true;
            }
        }
        return false;
    }
    @Transactional
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public boolean userEmailExist(String email){
        return findUserByEmail(email).isPresent();
    }
    public User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
