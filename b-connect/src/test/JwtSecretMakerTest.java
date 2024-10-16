public class JwtSecretMakerTest {

        @Test
        public  void generateSecretKey(){
            SecretKey key = Jwts.SIG.HS512.key().build();
            String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
            System.out.println(encodedKey);
            //97E59641510BBED33447C7C42EE5ECF8B739F36A875D699E7B3B9AC080E6323D94559FDD122DB188447100CA412B65F68A37811E2B9A002903DDD9400369FB02
        }
    }