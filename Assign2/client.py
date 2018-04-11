#All the imports goes here.
import urllib2, urllib
import json
import sys
from Crypto.PublicKey import RSA
from OpenSSL.crypto import load_publickey, load_privatekey, FILETYPE_PEM, verify, X509, sign
import string
import datetime

#Note public private key pairs for both client, server and KDC are computed beforehand.

#Reading the public key of KDC.
with open("kdcpublickey.pem") as f:
    KDC_public_key = f.read()


#Reading the private key of client in sign format.
with open("clientprivate.pem") as f:
    private_key_data = f.read()
private_key_sign = load_privatekey(FILETYPE_PEM, private_key_data)


#Creating a temporary certificate of KDC from public key. 
public_key_KDC = load_publickey(FILETYPE_PEM, KDC_public_key)
x509_KDC = X509()
x509_KDC.set_pubkey(public_key_KDC)


#Getting the public key of server from KDC.
target_url = "http://shantanu.pro/keydistributor.php"

#Generating data to be sent
files={}
files['file'] = "serverpublic.pem" 
mydata = urllib.urlencode(files)
print("Values sent to Key Distribution Centre:")
print("Data: " + mydata)

#Signing the data
my_sign = sign(private_key_sign,mydata,"SHA1")
print("My signature: ")
print(my_sign)
mydata = mydata+":"+my_sign


#Requesting the KDC to return the GMT servers key, digitally signed using RSA based private key.
req = urllib2.Request(target_url, mydata)
req.add_header("Content-type", "application/x-www-form-urlencoded")
result = urllib2.urlopen(req).read()
print("This is the response from the KDC: ")
print(result)


#Preprocessing the KDC's response for validity.
#If server finds that the integrity is compromised then it sends a warning message to client.
if result=="modified":
	print("Someone modified data please start again")
	sys.exit()

result = result.split(':',1)


#Verifying the data and signature are valid and integrity is maintained.
if not verify(x509_KDC, result[1][:-1], result[0], 'SHA1')== None:
	print("Someone modified data please start again")
	sys.exit()

print("Authentication and Message Integrity is verified in message from KDC. Congrats")
#This is the public key of server
print("Public key of server:\n"+result[0])
server_public = result[0]


#Requesting the GMT time server for current time
target_url = "http://www.shantanu.pro/gmtserver.php"
data = "time"
my_sign = sign(private_key_sign,data,"SHA1")
data = data+":"+my_sign

req = urllib2.Request(target_url, data)
req.add_header("Content-type", "application/x-www-form-urlencoded")
result = urllib2.urlopen(req).read()
print("This is the response from the GMT-Server: ")
print(result)

if result=="error":
	print("GMT server can't get my key")
	sys.exit()

if result=="modification":
	print("Someone modified data please start again")
	sys.exit()
	
result = result.split(':',1)


#Creating a temporary certificate of KDC from public key. 
public_key_KDC = load_publickey(FILETYPE_PEM, server_public)
x509_KDC = X509()
x509_KDC.set_pubkey(public_key_KDC)


#Verifying the data and signature are valid and integrity is maintained.
if not verify(x509_KDC, result[1], result[0], 'SHA1')== None:
	print("Someone modified data please start again")
	sys.exit()

print("Authentication and Message Integrity is verified in message from GMT server. Congrats")
#This is the public key of server


#Processing time and saving it in text file
time = string.replace(result[0],',',':')
print("Time recieved from server: "+time)
f= open("time.txt","w+")
f.write(time)
f.close()
