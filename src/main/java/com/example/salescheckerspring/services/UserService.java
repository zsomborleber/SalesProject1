package com.example.salescheckerspring.services;



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

import java.util.UUID;

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
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       // user.setEnabled(false);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);

        return userRepository.save(user);

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
        String subject = "Please verify your registration";
        String senderName = "Point of Sale Team";
        String mailContent = "<p>Dear " + user.getCompanyName() + "</p>";
        mailContent += "<p>Please click the link below to verify to your registration:</p>";
        String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
        mailContent += "<h3><a href=\"" + verifyUrl + "\">Verify</a></h3>";
        mailContent += "<p>Thank you<br>The Point of Sale Team </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("pointofsales2022@gmail.com",senderName);
        helper.setTo(user.getEmail());
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
    public User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
