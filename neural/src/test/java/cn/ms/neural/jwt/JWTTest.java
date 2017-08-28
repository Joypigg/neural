package cn.ms.neural.jwt;

import cn.ms.neural.jwt.algorithms.Algorithm;

public class JWTTest {

	public static void main(String[] args) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret");
			String token = JWT.create().withIssuer("auth0").sign(algorithm);

			System.out.println(token);
			
			JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build();
			DecodedJWT jwt = verifier.verify(token);
			System.out.println(jwt.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
