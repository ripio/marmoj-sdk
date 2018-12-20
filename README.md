# Marmoj-sdk
[![CircleCI](https://circleci.com/gh/ripio/marmoj-sdk.svg?style=shield)](https://circleci.com/gh/ripio/marmoj-sdk)


# Description
Marmoj-sdk helps to developers java/android to consume Marmo Relayer.

# Simple Summary
Allowing users to sign messages to show intent of execution, but allowing a third party relayer (https://github.com/ripio/marmo-relayer) 
to execute them is an emerging pattern being. This pattern simplifies the integration with any Ethereum based platform.

# Abstract
### User pain points:
- users don't want to think about ether
- users want to be able to pay for transactions using what they 
- Users don’t want to download apps/extensions (at least on the desktop) to connect to their apps

# Table of Contents
- In Work

# Ecosystem Graph
![](./images/01.png)

###### General WIKI ecosystem
- In work.
###### API layer
- Marmo relayer doc: (https://github.com/ripio/marmo-relayer/blob/master/README.md)
###### CORE layer
- Marmo contracts doc: (https://github.com/ripio/marmo-contracts/blob/master/README.md)

# Features
- Complete implementation of Intent for Marmo relay.
- Ethereum wallet support.
- Comprehensive integration tests demonstrating a number of the above scenarios.
- Android compatible.

##### It has four runtime dependencies:

- web3j (https://github.com/web3j).
- async-http-client (https://github.com/AsyncHttpClient/async-http-client).
- logback (https://github.com/qos-ch/logback).
- Jackson for JSON serialisation (https://github.com/FasterXML/jackson).

# Getting started

##### Prerequisites
* Java 8
* Maven

Typically your application should depend on release versions of marmoj, but you may also use snapshot dependencies
for early access to features and fixes, refer to the  `Snapshot Dependencies`_ section.

| Add the relevant dependency to your project:

##### Maven

Java 8:

```xml
   <dependency>
     <groupId>...</groupId>
     <artifactId>...</artifactId>
     <version>...</version>
   </dependency>
```

##### Gradle

Java 8:

```yaml
  compile ('...:...:...')
```
   
# how it works?

### Intent Flowchart
![](./images/02.png)

###### Dependencies
- T0  -> -
- T1  -> - 
- T2  -> T1
- T3  -> T1
- T4  -> T1
- T5  -> T1, T4
- T6  -> T2
- T7  -> T2
- T8  -> T2
- T9  -> T3
- T10 -> T4
- T11 -> T5
- T12 -> T6
- T13 -> T7
- T14 -> T8
- T15 -> T9
- T16 -> T10
- T17 -> T10
- T18 -> T11
- T19 -> T13
- T20 -> T14
- T21 -> T15
- T22 -> T18
- T23 -> T19
- T24 -> T21
- T25 -> T22
- T26 -> T12, T23, T20, T15
- T27 -> T24
- T28 -> T25
- T29 -> T27


### Build a intent
```java
String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN TOKEN
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";
ERC20 erc20 = new ERC20(tokenContractAddress); //

int value = 1;
IntentAction intentAction = erc20.transfer(
        new Address(to),
        new Uint256(value)
);

Credentials credentials = Credentials.create("Your credentials");

String contractAddress = "0x7c0e959d9b7c82d47c5013832f89282b8b773393";

// Intents ids dependency
List<byte[]> dependencias = Arrays.asList(
        Numeric.hexStringToByteArray("IntentId1"),
        Numeric.hexStringToByteArray("IntentId2"),
        Numeric.hexStringToByteArray("IntentIdN")
);

Intent intent = IntentBuilder.anIntent()
        .withDependencies(dependencias)
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withIntentAction(intentAction)
        .build();
```

### Sign a intent
```java
SignedIntent sign = MarmoUtils.sign(intent, credentials);
```

###  Send a intent
```java
IRelayClient client = new RelayClient("http://ec2-3-16-37-20.us-east-2.compute.amazonaws.com/relay"); //relay url
client.send(sign);
```

# Structure of builder

| Name                  | Type          | Mandatory | Default       | Description                                              |
| --------              | --------      | --------  | --------      | --------                                                 |
| id                    | byte[]        | yes       | Autogenerated | A unique identifier for the intent.                      |
| dependencies          | List<byte[]>  | no        | Empty         | Define a correlation id for intent.                      |
| signer                | String        | yes       | -             | The address of the signer that sign intent.              |
| wallet                | String        | yes       | -             | Contract address or Marmo instance.                      |
| salt                  | byte[]        | no        | 0x0           | Use for send same intent                                 |
| minGasLimit           | BigInteger    | no        | 0             | Minimum gas price.                                       |
| maxGasPrice           | BigInteger    | no        | 99999999      | Maximum gas price.                                       |
| intentAction          | IntentAction  | yes       | 0x0           | IntentAction Example -> network.marmoj.model.data.ERC20. |

# Examples

```java
/*
    Test with:
    - ERC20 Transfer
    - 1 Token
    - Signer
    - Wallet (0xDc3914BEd4Fc2E387d0388B2E3868e671c143944)
    - IntentAction (0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB, 1)
 */

String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN Token
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";

ERC20 erc20 = new ERC20(tokenContractAddress);
int value = 1;
IntentAction intentAction = erc20.transfer(
        new Address(to),
        new Uint256(value)
);

Credentials credentials = Credentials.create("512850c7ebe3e1ade1d0f28ef6eebdd3ba4e78748e0682f8fda6fc2c2c5b334a");
String contractAddress = "0xDc3914BEd4Fc2E387d0388B2E3868e671c143944";
Intent intent = IntentBuilder.anIntent()
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withIntentAction(intentAction)
        .build();

Assert.assertEquals(toHexString(intent.getId()), "0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928");


/*
    Test with:
    - balanceOf
    - Signer
    - Wallet (0x692a70d2e424a56d2c6c27aa97d1a86395877b3a)
    - IntentAction (0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB, 1)
 */

String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN TOKEN
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";

ERC20 erc20 = new ERC20(tokenContractAddress);
IntentAction intentAction = erc20.balanceOf(
        new Address(to)
);

Credentials credentials = Credentials.create("512850c7ebe3e1ade1d0f28ef6eebdd3ba4e78748e0682f8fda6fc2c2c5b334a");
String contractAddress = "0xbbf289d846208c16edc8474705c748aff07732db";
Intent intent = IntentBuilder.anIntent()
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withIntentAction(intentAction)
        .build();

Assert.assertEquals(toHexString(intent.getId()), "0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e");

/*
    Test with:
    - balanceOf
    - Signer
    - dependencies (0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e)
    - Wallet (0x692a70d2e424a56d2c6c27aa97d1a86395877b3a)
    - IntentAction (0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB, 1)
 */

String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN TOKEN
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";

ERC20 erc20 = new ERC20(tokenContractAddress);
IntentAction intentAction = erc20.balanceOf(
        new Address(to)
);

Credentials credentials = Credentials.create("512850c7ebe3e1ade1d0f28ef6eebdd3ba4e78748e0682f8fda6fc2c2c5b334a");
String contractAddress = "0xbbf289d846208c16edc8474705c748aff07732db";
Intent intent = IntentBuilder.anIntent()
        .withDependencies(Arrays.asList(hexStringToByteArray("0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e")))
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withIntentAction(intentAction)
        .build();

Assert.assertEquals(toHexString(intent.getId()), "0x19ca8e36872eaf21cd75c9319cfd08769b61fcb7c8ab119d71960c27585595af");


/*
    Test with:
    - balanceOf
    - Signer
    - dependencies (0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e, 0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928)
    - Wallet (0x692a70d2e424a56d2c6c27aa97d1a86395877b3a)
    - IntentAction (0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB, 1)
 */
String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN TOKEN
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";

ERC20 erc20 = new ERC20(tokenContractAddress);
IntentAction intentAction = erc20.balanceOf(
        new Address(to)
);

Credentials credentials = Credentials.create("512850c7ebe3e1ade1d0f28ef6eebdd3ba4e78748e0682f8fda6fc2c2c5b334a");
String contractAddress = "0xbbf289d846208c16edc8474705c748aff07732db";
List<byte[]> dependencies = Arrays.asList(
        hexStringToByteArray("0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e"),
        hexStringToByteArray("0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928")
);
Intent intent = IntentBuilder.anIntent()
        .withDependencies(dependencies)
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withIntentAction(intentAction)
        .build();

Assert.assertEquals(toHexString(intent.getId()), "0xab4b18a2b163ac552a6d2eac23529e4d5e25ff54c41831b75e8c169a03f39a20");


/*
    Test with:
    - balanceOf
    - Signer
    - dependencies (0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e, 0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928)
    - Wallet (0x692a70d2e424a56d2c6c27aa97d1a86395877b3a)
    - Min gas 300000
    - Max gas 999999
    - IntentAction (0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB, 1)
 */
String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN TOKEN
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";

ERC20 erc20 = new ERC20(tokenContractAddress);
IntentAction intentAction = erc20.balanceOf(
        new Address(to)
);

Credentials credentials = Credentials.create("512850c7ebe3e1ade1d0f28ef6eebdd3ba4e78748e0682f8fda6fc2c2c5b334a");
String contractAddress = "0xbbf289d846208c16edc8474705c748aff07732db";
List<byte[]> dependencies = Arrays.asList(
        hexStringToByteArray("0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e"),
        hexStringToByteArray("0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928")
);
Intent intent = IntentBuilder.anIntent()
        .withDependencies(dependencies)
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withMinGasLimit(BigInteger.valueOf(300000))
        .withMaxGasPrice(BigInteger.valueOf(999999))
        .withIntentAction(intentAction)
        .build();

Assert.assertEquals(toHexString(intent.getId()), "0x9ef832fe6023c21990339fe87724fe5a19fdb4697ce32769c238eb6ab9b92b2c");


/*
    Test with:
    - balanceOf
    - Signer
    - dependencies (0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e, 0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928)
    - Wallet (0x692a70d2e424a56d2c6c27aa97d1a86395877b3a)
    - Min gas 300000
    - Max gas 999999
    - IntentAction (0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB, 1)
    - Salt (0x0000000000000000000000000000000000000000000000000000000000000001)
 */

String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN TOKEN
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";

ERC20 erc20 = new ERC20(tokenContractAddress);
IntentAction intentAction = erc20.balanceOf(
        new Address(to)
);

Credentials credentials = Credentials.create("512850c7ebe3e1ade1d0f28ef6eebdd3ba4e78748e0682f8fda6fc2c2c5b334a");
String contractAddress = "0xbbf289d846208c16edc8474705c748aff07732db";
List<byte[]> dependencies = Arrays.asList(
        hexStringToByteArray("0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e"),
        hexStringToByteArray("0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928")
);
Intent intent = IntentBuilder.anIntent()
        .withDependencies(dependencies)
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withMinGasLimit(BigInteger.valueOf(300000))
        .withMaxGasPrice(BigInteger.valueOf(999999))
        .withIntentAction(intentAction)
        .withSalt(hexStringToByteArray("0x0000000000000000000000000000000000000000000000000000000000000001"))
        .build();

Assert.assertEquals(toHexString(intent.getId()), "0xfc1e9fd25abd26a1be78817f0675a5051285af23957ca0322f2925d93f291ec5");


/*
    Test with:
    - balanceOf
    - Signer
    - dependencies (0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e, 0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928)
    - Wallet (0x692a70d2e424a56d2c6c27aa97d1a86395877b3a)
    - Min gas 300000
    - Max gas 999999
    - IntentAction (0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB, 1)
    - Salt (0x0000000000000000000000000000000000000000000000000000000000000001)
 */
String tokenContractAddress = "0x2f45b6fb2f28a73f110400386da31044b2e953d4"; //RCN TOKEN
String to = "0x7F5EB5bB5cF88cfcEe9613368636f458800e62CB";

ERC20 erc20 = new ERC20(tokenContractAddress);
IntentAction intentAction = erc20.balanceOf(
        new Address(to)
);

Credentials credentials = Credentials.create("512850c7ebe3e1ade1d0f28ef6eebdd3ba4e78748e0682f8fda6fc2c2c5b334a");
String contractAddress = "0xbbf289d846208c16edc8474705c748aff07732db";
List<byte[]> dependencies = Arrays.asList(
        hexStringToByteArray("0xee2e1b62b008e27a5a3d66352f87e760ed85e723b6834e622f38b626090f536e"),
        hexStringToByteArray("0x6b67aac6eda8798297b1591da36a215bfbe1fed666c4676faf5a214d54e9e928")
);
Intent intent = IntentBuilder.anIntent()
        .withDependencies(dependencies)
        .withSigner(credentials.getAddress())
        .withWallet(contractAddress)
        .withMinGasLimit(BigInteger.valueOf(300000))
        .withMaxGasPrice(BigInteger.valueOf(999999))
        .withIntentAction(intentAction)
        .withSalt(hexStringToByteArray("0x0000000000000000000000000000000000000000000000000000000000000002"))
        .build();

Assert.assertEquals(toHexString(intent.getId()), "0xacd5d801cecc1790b95c5395e4f48a40d964ae0c6b70051b3c907060e67da079");

```