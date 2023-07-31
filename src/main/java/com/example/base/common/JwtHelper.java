package com.example.base.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class JwtHelper {
    public static String getJWTToken(String bearerToken) {
        String jwtToken = bearerToken.replace("Bearer ", "");
        return jwtToken;
    }

    public static String getUserName(String bearerToken) {
        DecodedJWT jwt = JWT.decode(getJWTToken(bearerToken));
        log.info("Get username - Token decryption: {}", jwt.getClaims());
        Claim emailClaim = jwt.getClaims().get("username");
        return emailClaim == null ? StringUtils.EMPTY : emailClaim.asString();
    }
}
