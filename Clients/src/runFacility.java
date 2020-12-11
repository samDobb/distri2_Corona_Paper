import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Calendar;

public class runFacility {


    public static void main(String[] args){

        byte[] date=java.util.Calendar.getInstance().getTime().toString().getBytes();

         String name="test";
        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecretKey masterSecretKey = keygen.generateKey();

            KeySpec spec = new PBEKeySpec(name.toCharArray(),date,1000, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);

            byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            String encodedLine = Base64.getEncoder().encodeToString(cipher.doFinal(masterSecretKey.getEncoded()));

            // reinit cypher using param spec
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            String out = new String(cipher.doFinal(Base64.getDecoder().decode(encodedLine)));

            System.out.println(java.util.Calendar.getInstance().getTime().toString());


        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
    }
}
