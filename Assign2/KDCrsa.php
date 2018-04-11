<?php

//Generating RSA based public private key pair for Key Distribution Centre
$res=openssl_pkey_new(array(
    "digest_alg" => "sha512",
    "private_key_bits" => 4096,
    "private_key_type" => OPENSSL_KEYTYPE_RSA,
));

openssl_pkey_export($res, $privkey);
$pubkey=openssl_pkey_get_details($res);
$pubkey=$pubkey["key"];


//Saving the public key of KDC in file
$my_file = 'kdcpublickey.pem';
$handle = fopen($my_file, 'w') or die('Cannot open file:  '.$my_file);
fwrite($handle, $pubkey);
fclose($handle);


//Saving the private key of KDC in file
$my_file = 'kdcprivatekey.pem';
$handle = fopen($my_file, 'w') or die('Cannot open file:  '.$my_file);
fwrite($handle, $privkey);
fclose($handle);

?>
