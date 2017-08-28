package cn.ms.neural.jwt.exceptions;

public enum ExceptionType {

	AlgorithmMismatch, InvalidClaim, JWTCreation, JWTDecode, JWTVerification, SignatureGeneration, SignatureVerification, TokenExpired;

}
