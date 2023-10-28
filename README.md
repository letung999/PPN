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

