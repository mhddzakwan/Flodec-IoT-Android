package com.belajar.flodec.firebase

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.collect.Lists
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class AccessToken {
    private val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"

    fun getAccessToken(): String?{
        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"flodec-p24s\",\n" +
                    "  \"private_key_id\": \"0952c02af0df9094c17b01b08469393a5e572def\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC6UBox7yY2QGRF\\nE+yxX1SsrKLPn+L4IuTfal4Ww02Yz9LZSLsgjxMKWr6IweUVs2VkLqkkDirBarhN\\n8gq65n5oWZCRfeBmDJvEoNJum3hIZFhWBhGDVYytyzsi7PEHDNNaxjGTGu6AkswQ\\nZg+kVIqOFi6jikhERqUEY5DOm8cu66LMyiamCPfIcQi0fFBR41927+PIZi60gn78\\nfQMleBGHns9ClVAPncKGiSdJAZqFZDJ+9WazrajPzwYEiNI2lyuSLQAf5BPdqTQ7\\nnMe+FAg4B5SpXMW5Tw6/Ht7x2obFLdRmUGUrdd94wsZs/fcatIPUgQRkBkbF02/1\\nR3b4PdwRAgMBAAECggEAKatXqHpdt/1/z4lMw0DYfcQgp8IRL8ESIju2bm3g0YAN\\nnsYq//Wv4v13pruC4m8ciqL69lLq0hZtwRUUvEdn1yvM7xVRllFUl04GsnE4Hiy/\\nKwIxcYe1LnuJLt8Cazq9nIw+MzHvTLdIR5SkA/Nah7vDU5VOY2LPbmNcVDE4A7CT\\nGh89o2iND2DNk7VPzuVydwd+x3xR57cHPWssmIumkV8FgTE6uRutooPQA+izB5bo\\nDGY2rZpWRJlNVhnHDLL9kyY2tU+RTLcPU4pTmzfNeId5joPMWa1bVhVfHYFn4d7b\\nL4zx4Vmv+CgnDRErR3J+bk6p5OfrxsgrRelF7bjpzwKBgQDh6ptBqbPJGD2Faw5F\\nZ+ygjBnMjeqFD5iVZNwVwkbwvCkhSrQ9Kabe9CvCBEAgJnaIMZe6PCVJvgLCOEkE\\nBiyokl2oUvJkik9UAwx2a2PENJW1rrI5ndgeVmDuPtESFaEzHe9LDX5/i3LIUfIS\\nZ1eM6YVwVo/lse9bMraBFSkS8wKBgQDTH222k8x0XjQJn0rA3Y8U+pLIPcGPtRaA\\nqSXiA3xQn6Doo8dmrJnPrxZzShrtrnVelYdT1TnU2Vbu2PiI9Nlykg+H8ZTw+2f8\\nu0gETurnFSoamz8GBn5jTEl+mYLDnbaC3kY7jK2+VJ5ZBunmR3ui0QKLJNxSaXJK\\n60TJpaht6wKBgQDWPMoQweLJj+A/lp9I+F2wfku07Nv6ZSjYe3YueDcX8ge7F4HH\\nAqnJgpnAepFpK7B6RTVSmdjujm9FSWUMmOJe1bZVept89qB11hJKdlp85TUXvtwq\\nwWICmv1+0TkowjRFKl0Tum88erxV9UfvZerjqUwulRgOxedrf7MMxElYOwKBgGqa\\ngAek8aQAvT3ulQx1033AWMWLiWVLByXIQzywjeEX+WP+7vzFQpU/z5I/ZQGHCfTM\\nhBysmhsD84V/QV/GYtYbMoRuU8qq54MR/fdO8gXlBfGRWm1scf0p1CJ82S51QGcs\\nXUJMcp6ck6hYeVqx1ZgM+b/QyP9EB1YVHhLD9xwpAoGBAM/V/bx4FXGbfVyTHrXP\\nbTVpKA0Xa7ThkrvB+N02Liy6oi6icQcf+j+06aXzjxzSVEVwAVPN7IJVAG6C8Yyx\\nMs7Ok5z0r98oI6XtJukwhrG6EhqXvXFLoBON8G4wHQZJsTcn4NtcxfG3rGOpwBcl\\nMOX6Y6h/ut6bax1n7P+rU0he\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-rkxif@flodec-p24s.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"104526632703082175800\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-rkxif%40flodec-p24s.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}"

            var stream : InputStream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val googleCredentials: GoogleCredentials = GoogleCredentials.fromStream(stream).createScoped(
                Lists.newArrayList(firebaseMessagingScope))

            googleCredentials.refresh()
            return googleCredentials.accessToken.tokenValue

        } catch (error: IOException){
            error.message?.let { Log.e("ErrorTOKEN", it.toString()) }
            return null
        }
    }


}