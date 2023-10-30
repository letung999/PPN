# PPN
>Overview description of this project: This a mock module that I have researched about technologies used in one of the modules of the C99 project at FPT Software and I have implemented it in this project. Main function of this project is send mail when the customer by a car.
# Technologies in used
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
    ```
    aws.paramstore.prefix=/ppn
    aws.paramstore.default-context=dev
    aws.paramstore.profile-separator=
    aws.paramstore.enabled=true

## Create RDS and Connect application with RDS.
- Create RDS and configure username, password, rules [RDS](https://aws.amazon.com/getting-started/hands-on/create-mysql-db/).
> **Notice** 
> You have to set up inbound rules for your RDS

