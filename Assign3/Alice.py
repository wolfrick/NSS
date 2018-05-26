#All imports goes here
import socket
import hmac
from Crypto.PublicKey import RSA
from Crypto import Random
import binascii
from Crypto.Hash import SHA256
from time import sleep


print "Initializing all the variables needed throughout the program"
#All the global variables needed in this assignment
myport = 11111
secret = binascii.hexlify(Random.get_random_bytes(16))
print "Secret Generated by Alice as:\t\t %s"%secret
prime = 7243469162906133262707138361729247674528418426076702186281286038623238274842547507072974617594640311
primitive = 3242736143229285405697273596419677873912657748731448981302390864459158863881443495029809033284732127
Xa = 27


print "\nPerforming basic functionalities not needed on fly"
#All global functionalities needed to be started in beginning
private_key_string = open("privateAlice.pem","r").read()
my_private_key = RSA.importKey(private_key_string)
public_key_string = open("publicBob.pem","r").read()
partner_public_key = RSA.importKey(public_key_string)


#Function to get secret shared key
def setSecret():
	serversocket = socket.socket()
	print "Port to be bound by me: %d" %myport
	serversocket.bind(('',myport))
	serversocket.listen(1)	
	enc_data = partner_public_key.encrypt(secret,32)
	msghash = SHA256.new(enc_data[0]).digest()
	signature = my_private_key.sign(msghash,'')
	newmsg = str(enc_data[0])+str(signature[0])
	connection,addr = serversocket.accept()
	connection.send(newmsg)
	connection.close()
	serversocket.close()
	

#Function for performing Elgamal cryptosystem
def initiate():
	global myport
	myport = myport +100
	serversocket = socket.socket()
	print "Port to be bound by me: %d" %myport
	serversocket.bind(('',myport))
	serversocket.listen(1)
	Ya = pow(primitive,Xa,prime)
	Ya = str(Ya)
	print "Public Key of Alice: Ya=%s" %Ya
	mac = hmac.new(secret)
	mac.update(Ya)
	msghmac = mac.hexdigest()
 	print "HMAC of Ya= %s" %msghmac
	tosend = Ya+":"+str(msghmac)
	connection,addr = serversocket.accept()
	connection.send(tosend)
	connection.close()
	serversocket.close()


def egcd(a, b):
    if a == 0:
        return (b, 0, 1)
    else:
        g, y, x = egcd(b % a, a)
        return (g, x - (b // a) * y, y)


def modinv(a, m):
    g, x, y = egcd(a, m)
    if g != 1:
        raise Exception('modular inverse does not exist')
    else:
        return x % m


def elgamalrecv():
	global myport
	myport = myport +100
	serversocket = socket.socket()
	print "\n\nPort to be bound by me: %d" %myport
	serversocket.bind(('',myport))
	serversocket.listen(1)
	connection,addr = serversocket.accept()	
	result = connection.recv(2048)
	result = result.split(':')
	C1 = result[0]
	mac = hmac.new(secret)
	mac.update(C1)
	msghmac = mac.hexdigest()
 	C1senthmac = result[1]
	C2 = result[2]
 	C2senthmac = result[3]
	if hmac.compare_digest(C1senthmac,msghmac)==False:
		print('Data modified in between...exiting')
		sys.exit(0)
	mac = hmac.new(secret)
	mac.update(C2)
	msghmac = mac.hexdigest()
	if hmac.compare_digest(C2senthmac,msghmac)==False:
		print('Data modified in between...exiting')
		sys.exit(0)
	C1 = int(C1)
	print "Received C1: %d"%C1
	
	C2 = int(C2)
	
	print "Received C2: %d"%C2
	
	K = pow(C1,Xa,prime)	
	invK = modinv(K,prime)	
	msg = (C2*invK)%prime
	received = str(msg)
	msg = received[1:]
	print "Message recieved is: %s"%msg
	temp = int(msg,2)	
	recvstr = binascii.unhexlify('%x'%temp)
	print "Converting back to string msg: %s" %recvstr
	connection.close()
	serversocket.close()
	


def main():
	setSecret()
	initiate()
	elgamalrecv()
	elgamalrecv()
	elgamalrecv()

main()
