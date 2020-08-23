package br.com.encryption.aws.exercise.clientsideencryption.encryption;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Component
public class EncryptionManager {

    private final String region = System.getenv("AWS_REGION");
    private final String keyArn = System.getenv("AWS_KMS_KEY_ARN");

    public String encrypt(String message) {
        return encryptData(message);
    }

    private String encryptData(String message) {
        // Instantiate the SDK
        final AwsCrypto crypto = new AwsCrypto();

        // 2. Instantiate a KMS master key provider
        final KmsMasterKeyProvider masterKeyProvider = KmsMasterKeyProvider
                .builder()
                .withKeysForEncryption(keyArn)
                .withDefaultRegion(region)
                .build();

        // 3. Create an encryption context
        //
        // Most encrypted data should have an associated encryption context
        // to protect integrity. This sample uses placeholder values.
        //
        // For more information see:
        // blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management
        final Map<String, String> encryptionContext = Collections.singletonMap("Example", "String");

        // 4. Encrypt the data
        final CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(masterKeyProvider, message.getBytes(StandardCharsets.UTF_8), encryptionContext);
        final byte[] cipherText = encryptResult.getResult();

        System.out.println(new String(cipherText));

        // 5. Decrypt the data
        final CryptoResult<byte[], KmsMasterKey> decryptResult = crypto.decryptData(masterKeyProvider, cipherText);

        // 6. Before verifying the plaintext, verify that the customer master key that
        // was used in the encryption operation was the one supplied to the master key provider.
        if (!decryptResult.getMasterKeyIds().get(0).equals(keyArn)) {
            throw new IllegalStateException("Wrong key ID!");
        }

        // 7. Also, verify that the encryption context in the result contains the
        // encryption context supplied to the encryptData method. Because the
        // SDK can add values to the encryption context, don't require that
        // the entire context matches.
        if (!encryptionContext.entrySet().stream()
                .allMatch(e -> e.getValue().equals(decryptResult.getEncryptionContext().get(e.getKey())))) {
            throw new IllegalStateException("Wrong Encryption Context!");
        }

        // 8. Verify that the decrypted plaintext matches the original plaintext
        System.out.println(new String(decryptResult.getResult()).equals("message"));

        return new String(decryptResult.getResult());
    }
}
