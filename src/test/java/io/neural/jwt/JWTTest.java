package io.neural.jwt;

import io.neural.jwt.DecodedJWT;
import io.neural.jwt.JWT;
import io.neural.jwt.JWTVerifier;
import io.neural.jwt.algorithms.Algorithm;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTTest {

	public static void main(String[] args) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			String token = JWT.create().withIssuer("auth0").sign(algorithm);

			System.out.println(token);

			JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
			DecodedJWT jwt = verifier.verify(token);
			System.out.println(jwt.toString());

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(jwt);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
