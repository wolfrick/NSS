<?php

//Generating RSA based public private key pair for server
$res=openssl_pkey_new(array(
    "digest_alg" => "sha512",
    "private_key_bits" => 2048,
    "private_key_type" => OPENSSL_KEYTYPE_RSA,
));
openssl_pkey_export($res, $privkey);
$pubkey=openssl_pkey_get_details($res);
$pubkey=$pubkey["key"];


//Saving the servers public key in file
$my_file = 'serverpublic.pem';
$handle = fopen($my_file, 'w') or die('Cannot open file:  '.$my_file);
fwrite($handle, $pubkey);
fclose($handle);


//Saving the servers private key in file
$my_file = 'serverprivate.pem';
$handle = fopen($my_file, 'w') or die('Cannot open file:  '.$my_file);
fwrite($handle, $privkey);
fclose($handle);

?>
