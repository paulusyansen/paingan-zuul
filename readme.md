# Paingan Microservices
| project | repo | port |
|------ | ------ | ------ |
|paingan-zuul (gateway)|https://github.com/paulusyansen/paingan-zuul|8700|
|paingan-eureka (service discovery)|https://github.com/paulusyansen/paingan-eureka|8900|
|paingan-oauth2 (authentication server)|https://github.com/paulusyansen/paingan-oauth2|8400|
|paingan-member-service|https://github.com/paulusyansen/paingan-member-service|8100|
|paingan-order-service|https://github.com/paulusyansen/paingan-order-service|8300|
|paingan-config-server|on progress||
|paingan-angular-ms|on progress||


## Tools
- eclipse sts
- maven
- git
- docker
- rabbitmq
- zipkin

## Testing Account
- admin/password ~ role_admin
- user/password ~ role_user

## Others

#### rabbitmq
```sh
$ brew services start rabbitmq
```
```sh
http://localhost:15672 guest/guest
```

#### zipkin
```sh
$ RABBIT_URI=amqp://localhost java -jar zipkin-server-2.11.5-exec.jar 
```
```sh
http://localhost:9411/zipkin/
```

#### docker
```sh
docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer
```
```sh
http://localhost:9000
```