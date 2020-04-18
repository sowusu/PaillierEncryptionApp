# PaillierEncryptionApp
A simple app that uses the Paillier Encryption Toolbox to perform addition of ciphertext

# Running app

Run the below commands with the correct number of commandline parameters in the order shown below:

1. java -jar hw3q1.jar -keygen -outputPK public-key-file -outputPr private-key-file 

2. java -jar hw3q1.jar -encrypt -pk public-key-file -input input-file -output enc-file

3. java -jar hw3q1.jar -process -pk public-key-file -input enc-file -output stats-file

4. java -jar hw3q1.jar -decrypt -pr private-key-file -input enc-file -output dec-file -pk public-key-file

Note that there is an additional cli parameter for public key for decryption since the PaillierPrivateKey constructor requires both the public and private keys.
