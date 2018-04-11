<?php
//Reading input data.
$data = file_get_contents('php://input');


//Preprocessing the data.
$pos = strpos($data,':');
$signature = substr($data,$pos+1);
$file = substr($data,5,$pos-5);


//Reading the public key of client.
if(strcmp($file,"serverpublic.pem")==0)
	$key_file = "clientpublic.pem";
else
	$key_file = "serverpublic.pem";
$client_key = openssl_pkey_get_public(file_get_contents($key_file)); 
$client_public_key = openssl_pkey_get_details($client_key);


//Reading private key of KDC. 
$priv_key = openssl_pkey_get_private(file_get_contents("kdcprivatekey.pem")); 
openssl_pkey_export($priv_key,$mykey);


//Verifying the client signature.
$toverify = substr($data,0,$pos);
$ok = openssl_verify($toverify, $signature, $client_public_key['key']);	
if($ok)
{
	//Reading the required file to treat as the data to be transferred  
	$handle = fopen($file, 'r') or die('Cannot open file:  '.$data);
	$result = fread($handle, filesize($file));
	fclose($handle);
	

	//Signing and sending the data to be sent to client
	$res = openssl_sign($result,$enc,$mykey);
	$tosend = $result.':'.$enc;
	echo $tosend;
}
else
{
	//Verification of client signature failed. (Invalid user or Someone modified the data in between)
	echo $toverify;
}
?>

