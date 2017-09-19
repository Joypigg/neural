package io.neural.jwt.exceptions;

public enum ExceptionType {

	AlgorithmMismatch, InvalidClaim, JWTCreation, JWTDecode, JWTVerification, SignatureGeneration, SignatureVerification, TokenExpired;

}
