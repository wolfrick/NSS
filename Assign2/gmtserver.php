<?php

//Reading input from client
$clientdata = file_get_contents('php://input');


//Contacting the KDC to get the public key of client
//Getting servers private key and signing the data
$priv_key = openssl_pkey_get_private(file_get_contents("serverprivate.pem")); 
openssl_pkey_export($priv_key,$mykey);
$content = 'file=clientpublic.pem';
$res = openssl_sign($content,$enc,$mykey);
$tosend = $content.':'.$enc;


//Making curl request to the KDC to get client public key
$ch = curl_init('http://www.shantanu.pro/keydistributor.php');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
curl_setopt($ch, CURLOPT_POSTFIELDS, $tosend);
$output = curl_exec($ch);
curl_close($ch);

$client_key = openssl_pkey_get_public(file_get_contents("kdcpublickey.pem")); 
$client_public_key = openssl_pkey_get_details($client_key);


//Preprocessing the response of KDC
$pos = strpos($output,':');
$signature = substr($output,$pos+1);
$signature = rtrim($signature);


//Verifying the KDC signature
$toverify = substr($output,0,$pos);
$ok = openssl_verify($toverify, $signature, $client_public_key['key']);	
if($ok)
{
    $client_key = $toverify;
    $pos = strpos($clientdata,':');
    $signature = substr($clientdata,$pos+1);
    
    
    //Verifying client signature
    $toverify = substr($clientdata,0,$pos);
    $ok = openssl_verify($toverify, $signature, $client_key);	
    if($ok)
    {
	//Sending the data to client in correct format
        $tosend = date('Y/m/d/H/i/s');
        $res = openssl_sign($tosend,$enc,$mykey);
        $tosend = $tosend.':'.$enc;
        echo $tosend;
    }
    else
    {
        //Someone tried to change data in between GMT server and Client	
        echo "modification";
    }
}
else
{
    //Someone tried to change data in between KDC and GMT server
    echo "error";    
}
?>
