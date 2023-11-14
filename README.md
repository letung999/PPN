# PPN
>Overview description of this project: This a mock module that I have researched about technologies used in practical projects and I have implemented it in this project.
# Technologies are used
- Use Java version 17, Spring framework version 3
- Use services of AWS
  * RDS
  * CloudWatch
  * SSM
  * BeanStalk
  * IAM
  * EC2
- Setup CI/CD deploy application
  * GitHub Action
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/3ab0c21b73442dab3ccf658db810a7f21aeea5cf/images/JavaCI.png?raw=true">
        <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/3ab0c21b73442dab3ccf658db810a7f21aeea5cf/images/JavaCI.png?raw=true">
        <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/3ab0c21b73442dab3ccf658db810a7f21aeea5cf/images/JavaCI.png?raw=true">
    </picture>
  * Terraform
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/TerraformCD.png?raw=true">
        <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/TerraformCD.png?raw=true">
        <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/TerraformCD.png?raw=true">
    </picture>
- Redis cache data.
    * install and start Redis
        * `npm install -g redis-commander`
        * `redis-commander`
        *  start command line and access `localhost` and you can see this through picture below:
        <picture>
                <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/Redis.png?raw=true">
                <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/Redis.png?raw=true">
                <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/Redis.png?raw=true">
        </picture>
- Separate environments to testing.
  * qa
  * silo-5
  * prod
  * uat
- Use Splunk to monitoring logs
- Use Thymeleaf as template to send mail
- Use logback and log4j2.

# Steps by Steps to Implement.

## Set up to connect application with SSM AWS and create parameter store.
    * Import dependencies
> **Important**
> attention to the Java version and Spring boot version, maybe when you import dependencies will occur error because of not integrated version.
  ``` java
  <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-bootstrap</artifactId>
      </dependency>

      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-aws-parameter-store-config</artifactId>
          <version>2.2.6.RELEASE</version>
      </dependency>
  ...
  <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-dependencies</artifactId>
              <version>${spring-cloud.version}</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
      </dependencies>
  </dependencyManagement>
  ```
- Set up environment in intellij to connect AWS
    * `edit configuration -> modify options -> Enviroment Variables`
    <picture>
            <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/letung999/PPN/cbde070e36c6f8be4724dfd36c01c6d958e275e3/images/EnviromentIntellj_AWS.png">
            <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/letung999/PPN/cbde070e36c6f8be4724dfd36c01c6d958e275e3/images/EnviromentIntellj_AWS.png">
            <img alt="The follow of system" src="https://raw.githubusercontent.com/letung999/PPN/cbde070e36c6f8be4724dfd36c01c6d958e275e3/images/EnviromentIntellj_AWS.png">
    </picture>
- Create parameter store in SSM
    * create parameter store [SSM](https://docs.aws.amazon.com/systems-manager/latest/userguide/parameter-create-console.html)
    <picture>
            <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/letung999/PPN/cbde070e36c6f8be4724dfd36c01c6d958e275e3/images/SSM.png">
            <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/letung999/PPN/cbde070e36c6f8be4724dfd36c01c6d958e275e3/images/SSM.png">
            <img alt="The follow of system" src="https://raw.githubusercontent.com/letung999/PPN/cbde070e36c6f8be4724dfd36c01c6d958e275e3/images/SSM.png">
    </picture>
    
- Custom name of parameter store by creating: `bootstrap.properties`
    ``` properties
    aws.paramstore.prefix=/ppn
    aws.paramstore.default-context=dev
    aws.paramstore.profile-separator=
    aws.paramstore.enabled=true

## Create RDS and Connect application with RDS.
- Create RDS and configure username, password, rules [RDS](https://aws.amazon.com/getting-started/hands-on/create-mysql-db/).
> **Note** 
> You have to set up inbound rules for your RDS and public accessible.
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/Rules_RDS.png?raw=true">
        <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/Rules_RDS.png?raw=true">
        <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/Rules_RDS.png?raw=true">
    </picture>
> after we created success as this below:
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/RDS.png?raw=true">
        <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/RDS.png?raw=true">
        <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/RDS.png?raw=true">
    </picture>
> We can monitoring status and workload database.
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/RDS_monitoring.png?raw=true">
        <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/RDS_monitoring.png?raw=true">
        <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/RDS_monitoring.png?raw=true">
    </picture>


- Connect MySQL with RDS: go ahead AWS RDS and copy the endpoint and paste it into hostname at MySQL workbench.
    <picture>
          <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/letung999/PPN/dev/images/RDS_local.png">
          <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/letung999/PPN/dev/images/RDS_local.png">
          <img alt="The follow of system" src="https://raw.githubusercontent.com/letung999/PPN/dev/images/RDS_local.png">
    </picture>
## Separate environment for parameter store in SSM


- create `application-prod.properties`, `bootstrap-prod.properties` and then use `spring.profiles.active=name_enviroment`or you can set up in active environment in intellij IDEA
- `application-prod.properties`
    ``` properties
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=${url-db}
    spring.datasource.username=${username}
    spring.datasource.password=${password-db}
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=false
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL55Dialect
    spring.jpa.properties.hibernate.globally_quoted_identifiers=true
    server.port=5000
    ```
- `bootstrap-prod.properties`
    ``` properties
    aws.paramstore.prefix=/ppn
    aws.paramstore.default-context=prod
    aws.paramstore.profile-separator=
    aws.paramstore.enabled=true
    ```

## Set up CI/CD to deploy application.

* create `maven.yml` and in this file you'll config steps and connect to AWS:
```yaml

name: ppn

on:
  push:
    branches: [ "master" ]
  pull_request:
    branches: [ "master" ]

jobs:
  build:

    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      - name: Build with Maven
        run: mvn clean install

      - name: Prepare S3 upload target
        run: mkdir artifacts && cp target/*.jar artifacts/

      - name: install aws cli
        run: sudo apt-get update && sudo apt-get install -y awscli

      - name: Set up aws credentials
        run: |
          mkdir -p ~/.aws
          touch ~/.aws/credentials
          echo "[default]
          aws_access_key_id = ${AWS_ACCESS_KEY_ID}
          aws_secret_access_key = ${AWS_SECRET_ACCESS_KEY}" > ~/.aws/credentials
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY}}
          AWS_S3_BUCKET: ${{ secrets.AWS_S3_BUCKET}}

      - name: Copy files to S3
        run: aws s3 cp artifacts s3://${{ secrets.AWS_S3_BUCKET }}/${GITHUB_SHA::7}/ --recursive --region us-east-1

      # Optional: Uploads the full dependency graph to GitHub to improve the quality of Dependabot alerts this repository can receive
      - name: Update dependency graph
        uses: advanced-security/maven-dependency-submission-action@571e99aab1055c2e71a1e2309b9691de18d6b7d6

```
* create `terraform.yml` to manage AWS, it will demonstrate through three steps:
  * `terraform init`
  * `terraform plan`
  * `terraform apply`

```yaml
name: 'Deploy PPN with Terraform'

on:
  push:
    branches: [ "master" ]
  pull_request:

permissions:
  contents: read

jobs:
  deploy_terraform:
    name: Deploy with terraform
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: hashicorp/setup-terraform@v1
      - name: Set up AWS credentials
        run: |
          mkdir -p ~/.aws
          touch ~/.aws/credentials
          echo "[default]
          aws_access_key_id = ${AWS_ACCESS_KEY_ID}
          aws_secret_access_key = ${AWS_SECRET_ACCESS_KEY}" > ~/.aws/credentials
        env: 
          AWS_ACCESS_KEY_ID: ${{ secrets.DEPLOY_AWS_TERRAFORM_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.DEPLOY_AWS_TERRAFORM_SECRET_ACCESS_KEY }}

      - name: "intialize terraform"
        run: terraform init

      - name: "validate terraform"
        run: terraform validate -no-color

      - name: "Run terraform apply"
        run: terraform apply -auto-approve -no-color

```
* If you set up success and create a PR to testing:
  <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/CICD.png?raw=true">
      <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/CICD.png?raw=true">
      <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/CICD.png?raw=true">
  </picture>

## Set up logback.xml to send log from application to CloudWatch

* Step 1: You will go ahead cloud watch of AWS and create a log group, each log group will have a lot of log streams corresponding to environments.
  
<picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/CreateLogGroup_Streams.png?raw=true">
      <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/CreateLogGroup_Streams.png?raw=true">
      <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/CreateLogGroup_Streams.png?raw=true">
</picture>

* Step 2: Create logback.xml file in resources folder simple like that:

```xml
<configuration>
    <appender name="cloudwatch" class="com.ppn.ppn.utils.CloudWatchAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
    </appender>

    <root level="info">
        <appender-ref ref="cloudwatch"/>
    </root>
</configuration>
```

* Step 3: Create CloudWatchAppender class to handle logs, you can see this class in my sources code.

 <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/Cloud_WatchLog.png?raw=true">
      <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/Cloud_WatchLog.png?raw=true">
      <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/Cloud_WatchLog.png?raw=true">
  </picture>

* Step 4: When you can set up successfully, you can monitoring logs of application in cloud watch, if you want to look for a log, only click Logs insights tab and use the command line with filter by correlationId attached each request.

<picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/dev/images/LogInsight.png?raw=true">
      <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/dev/images/LogInsight.png?raw=true">
      <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/dev/images/LogInsight.png?raw=true">
</picture>
