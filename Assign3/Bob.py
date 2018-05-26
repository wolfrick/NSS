#All imports goes here
import socket
import hmac
from Crypto.PublicKey import RSA
from Crypto import Random
from random import randint
import binascii
from Crypto.Hash import SHA256
import sys
from time import sleep


#All the global variables needed in this assignment
print "Initializing all the variables needed throughout the program"
partnerport = 11111
secret = 0
prime = 7243469162906133262707138361729247674528418426076702186281286038623238274842547507072974617594640311
primitive = 3242736143229285405697273596419677873912657748731448981302390864459158863881443495029809033284732127
Ya = 0


#All global functionalities needed to be started in beginning
print "\nPerforming basic functionalities not needed on fly"
private_key_string = open("privateBob.pem","r").read()
my_private_key = RSA.importKey(private_key_string)
public_key_string = open("publicAlice.pem","r").read()
partner_public_key = RSA.importKey(public_key_string)


#Function to perform deffiehellman key generation/exchange
def getSecret():
	clientsocket = socket.socket()
	clientsocket.connect(('127.0.0.1', partnerport))			
	result = clientsocket.recv(2048)
	enc_data = result[:512]
	recvsig = result[512:]
	msghash = SHA256.new(enc_data).digest()
	ver = partner_public_key.verify(msghash,(long(recvsig),))
	if ver == False:
		print "Someone modified data in between exiting"
		sys.exit(0)
	global secret
	secret = my_private_key.decrypt(enc_data)	
	print "secret recieved is: %s" %secret
	clientsocket.close()


def initiate():
	global partnerport
	partnerport = partnerport + 100
	clientsocket = socket.socket()
	clientsocket.connect(('127.0.0.1', partnerport))
	result = clientsocket.recv(2048)
	result = result.split(':',1)
	global Ya
	Ya = int(result[0])
	sentmac = str(result[1])
	mac = hmac.new(secret)
	mac.update(str(Ya))
	msghmac = mac.hexdigest()
	if hmac.compare_digest(sentmac,msghmac)==False:
		print "Someone modified data in between...exiting"
		sys.exit(0)
	print "Recieved Ya: %d"%Ya
	
	
#Function for performing Elgamal cryptosystem
def elgamal(msg):
	global partnerport
	partnerport = partnerport + 100
	clientsocket = socket.socket()
	clientsocket.connect(('127.0.0.1', partnerport))
	print "\n\nConnecting to port: %d" %partnerport
	print "Message to be sent: %s" %msg
	tosend = ''.join(format(ord(i),'b').zfill(8) for i in msg)
        tosend = '1' + tosend
	msg = int(tosend) 
	k = randint(2,prime-1)	
	print "Selected k is: %d" %k
	K = pow(Ya,k,prime)
	C1 = str(pow(primitive,k,prime))
	print "C1 to be sent: %s"%C1
	mac = hmac.new(secret)
	mac.update(C1)
	C1hmac = mac.hexdigest()
	C2 = str((K*msg)%prime)
	print "C2 to be sent: %s"%C2
	mac = hmac.new(secret)
	mac.update(C2)
	C2hmac = mac.hexdigest()
	tosend = C1+":"+C1hmac+":"+C2+":"+C2hmac
	clientsocket.send(tosend)
	clientsocket.close()
	

def main():
	getSecret()
	sleep(0.5)
	initiate()
	sleep(1)
	elgamal('hello 1')
	sleep(1)
	elgamal('hello 2')
	sleep(1)
	elgamal('hello 3')


main()
