package com.company.service;

import com.company.DataAccess;
import com.company.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Connection con = new DataAccess().getConnection();

    @Override
    public void addUser(String phone, String id_number, String password) throws Throwable {
        String query = "insert into public.user values (?, ?, ?, ?)";
        validateIdNumber(id_number);
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setObject(1, UUID.randomUUID());
            preparedStatement.setString(2, id_number);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, MD5(password));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String loginUser(String id_number, String password) throws Throwable {
        String token = generateJWTToken(id_number);
        validateUser(id_number, password);
        try (Statement st = con.createStatement()) {
            st.executeUpdate(String.format("update public.user set token = '%s' where id_number = '%s'", token, id_number));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public Map<String, String> verifyUser(String token) throws Throwable {
        Map<String, String> map = new HashMap<>();
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(
                     "SELECT * FROM public.user where token = '%s'", token))) {
            if ( !resultSet.isBeforeFirst() ) {
                throw new IllegalArgumentException("user not found");
            }
            while (resultSet.next()) {
                map.put("phone", resultSet.getString("phone"));
                map.put("id_number", resultSet.getString("id_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }
    @Override
    public User getUserByToken(String token) throws Throwable {
        User user = new User();
        String query = String.format("SELECT * FROM public.user where token = '%s'", token);
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (!resultSet.isBeforeFirst()) {
                throw new IllegalArgumentException("user not found");
            }
            while (resultSet.next()) {
                user.setId((UUID) resultSet.getObject("id"));
                user.setId_number(resultSet.getString("id_number"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setToken(resultSet.getString("token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private void validateIdNumber(String id_number) throws Throwable {
        if ( id_number.length() != 10 || !id_number.matches("\\d+") )
            throw new IllegalArgumentException("Invalid id number");
    }

    private void validateUser(String id_number, String password) {
        String pwd = MD5(password);
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(
                     "SELECT * FROM public.user where id_number = '%s' and password = '%s'", id_number, pwd))) {
            if ( !resultSet.isBeforeFirst() ) {
                throw new IllegalArgumentException("user not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateJWTToken(String id_number) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("testToken")
                .setSubject(id_number)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
        return "Bearer " + token;
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}