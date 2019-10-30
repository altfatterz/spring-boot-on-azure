### Spring Boot app running as an App Service on Azure Cloud

#### Install the Azure CLI

```bash
$ brew install azure-cli
```

#### Login

```bash
$ az login
```

```bash
$ az account list --output table

Name        CloudName    SubscriptionId                        State    IsDefault
----------  -----------  ------------------------------------  -------  -----------
Free Trial  AzureCloud   d72c2cf1-f779-439f-8bd6-a3ba1d0290c2  Enabled  True
```

#### Create an Azure service principal

```bash
$ az ad sp create-for-rbac --name "SpringBootOnAzureSP"  
```

This creates a service principal under our subscription `/subscriptions/d72c2cf1-f779-439f-8bd6-a3ba1d0290c2` 

```json
{
  "appId": "2ebba034-4d53-4f24-a7d6-18cd1a5a1730",
  "displayName": "SpringBootOnAzureSP",
  "name": "http://SpringBootOnAzureSP",
  "password": "e7a59d5c-26cf-44f4-b667-118c4a2d4d2d",
  "tenant": "f3730dbf-6176-4a2e-93c0-f762af479d33"
}
```

#### Configure Maven's settings.xml

Add a `server` element into your Maven `settings.xml` file where the you map the followings:
`client` --> taken from `appId`
`tenant` --> taken from `tenant`
`key` --> taken from `password`
Set the `environemnt` to `AZURE`

```xml
<servers>
   <server>
     <id>azure-authentication</id>
      <configuration>
         <client>2ebba034-4d53-4f24-a7d6-18cd1a5a1730</client>
         <tenant>f3730dbf-6176-4a2e-93c0-f762af479d33</tenant>
         <key>e7a59d5c-26cf-44f4-b667-118c4a2d4d2d</key>
         <environment>AZURE</environment>
      </configuration>
   </server>
</servers>
```

#### Clone the project 

```bash
$ https://github.com/altfatterz/spring-boot-on-azure.git
```

#### Configure the `pom.xml`

Configure the `azure-webapp-maven-plugin` plugin to use the `azure-authentication` server Maven setting.

```xml
<plugin>
  <groupId>com.microsoft.azure</groupId>
  <artifactId>azure-webapp-maven-plugin</artifactId>
  <version>1.8.0</version>
  <configuration>
    <!-- Reference <serverId> in Maven's settings.xml to authenticate with Azure -->
    <authentication>
        <serverId>azure-authentication</serverId>
    </authentication>
    ...
  </configuration>
</plugin>
```

Review the `azure-webapp-maven-plugin` configuration in the project for further details.

#### Deploy the app

```bash
$ mvn azure-webapp:deploy
```

#### Further TODOs

