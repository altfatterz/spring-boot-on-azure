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

...
[WARNING] You are using an old way of authentication which will be deprecated in future versions, please change your configurations.
[INFO] Authenticate with ServerId: azure-authentication
[INFO] [Correlation ID: 5b9fb931-2566-4c2c-9f6d-0517109588ae] Instance discovery was successful
[INFO] Target Web App doesn't exist. Creating a new one...
[INFO] Creating App Service Plan 'ServicePlan11334dea-dfce-4402'...
[INFO] Successfully created App Service Plan.
[INFO] Successfully created Web App.
[INFO] Stopping Web App before deploying artifacts...
[INFO] Successfully stopped Web App.
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource to /Users/zoal/projects/spring-boot-on-azure/target/azure-webapp/spring-boot-on-azure-20191030152029245-b6c868e7-3ee9-4542-9f49-1b4c1f0868e3
[INFO] Trying to deploy artifact to spring-boot-on-azure-20191030152029245...
[INFO] Renaming /Users/zoal/projects/spring-boot-on-azure/target/azure-webapp/spring-boot-on-azure-20191030152029245-b6c868e7-3ee9-4542-9f49-1b4c1f0868e3/spring-boot-on-azure-0.0.1-SNAPSHOT.jar to app.jar
[INFO] Deploying the zip package spring-boot-on-azure-20191030152029245-b6c868e7-3ee9-4542-9f49-1b4c1f0868e31007581442608081701.zip...
[INFO] Exception occurred during deployment: java.net.SocketTimeoutException: timeout, retry immediately(1/3)...
[INFO] Successfully deployed the artifact to https://spring-boot-on-azure-20191030152029245.azurewebsites.net
[INFO] Starting Web App after deploying artifacts...
[INFO] Successfully started Web App.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:45 min
[INFO] Finished at: 2019-10-30T16:22:15+01:00
```

#### Try out the service

```bash
http https://spring-boot-on-azure-20191030152029245.azurewebsites.net

HTTP/1.1 200 OK
Content-Length: 53
Content-Type: text/plain;charset=UTF-8
Date: Wed, 30 Oct 2019 15:30:43 GMT
Set-Cookie: ARRAffinity=2341b754ef97daa69db43f3b1555b5c9df8f30bc228f0c2a8a16f7cda08e30fa;Path=/;HttpOnly;Domain=spring-boot-on-azure-20191030152029245.azurewebsites.net

Greetings from Azure Cloud, running as an App Service
```

### Verify the Azure Portal

The created `spring-boot-on-azure` resource group

!(app-service-plan)[https://raw.githubusercontent.com/altfatterz/spring-boot-on-azure/master/images/app-service-plan.png]

The created `App Service`

!(app-service)[https://raw.githubusercontent.com/altfatterz/spring-boot-on-azure/master/images/app-service.png]

The created 

#### Further TODOs

Check the warning:

```bash
[WARNING] You are using an old way of authentication which will be deprecated in future versions, please change your configurations.
```