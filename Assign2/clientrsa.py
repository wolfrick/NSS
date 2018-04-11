from Crypto.PublicKey import RSA
from Crypto import Random

#Generating Random RSA public private key pair
random_generator = Random.new().read
key = RSA.generate(2048, random_generator)
public_key = key.publickey()


#Saving public key in file
f = open("clientpublic.pem","w")
f.write(public_key.exportKey())
f.close()


#Saving private key in file
f = open("clientprivate.pem","w")
f.write(key.exportKey())
f.close()
