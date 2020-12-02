package com.company.service;

import com.company.DataAccess;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.company.model.User;

@Service
public class UserServiceImpl implements UserService {

    private Connection con = new DataAccess().getConnection();

    @Override
    public void addUser(String phone, String id_number, String password) {
        String query = "insert into public.user values (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setObject(1, UUID.randomUUID());
            preparedStatement.setString(2, id_number);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, password);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    @Override
    public String loginUser(String id_number, String password) {
        String token = generateJWTToken(id_number);
        try (Statement st = con.createStatement()){
            st.executeUpdate(String.format("update public.user set token = '%s' where id_number = '%s'", token, id_number));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
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

	@Override
    public User verifyUser(String token) {
		User user = new User();

		try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM public.user where token = '%s'", token))) {
                while (resultSet.next()) {
                    user.setPhone( resultSet.getString("phone"));
                    user.setId_number( resultSet.getString("id_number"));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

		return user;
	}

}