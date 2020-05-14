package utils;

import java.security.SecureRandom;
import java.util.Arrays;

public class UserToken {
	
	/**
	 * The random used to generate tokens.
	 */
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * The size of a token in bytes.
	 */
	private static final int TOKEN_SIZE = 24;
	
	/**
	 * This token's byte make-up.
	 */
	private final byte[] bytes;
	
	/**
	 * Creates a new token with the given byte array.
	 * @param bytes The token data.
	 */
	public UserToken(byte[] bytes) {
		this.bytes = bytes;
	}
	
	/**
	 * Returns this token's byte array representation.
	 * @return [<b>byte[]</b>] The bytes making up this token.
	 */
	public final byte[] getBytes() {
		return Arrays.copyOf(bytes, bytes.length);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(!(obj instanceof UserToken))
			return false;
		
		UserToken token = (UserToken) obj;
		return Arrays.equals(token.bytes, bytes);
	}
	
	/**
	 * Generates a new token.
	 * @return [{@link UserToken}] The newly generated token.
	 */
	public static UserToken create() {
		byte[] buffer = new byte[TOKEN_SIZE];
		RANDOM.nextBytes(buffer);
		return new UserToken(buffer);
	}
	
}
