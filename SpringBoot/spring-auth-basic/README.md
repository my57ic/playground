Usage example

`curl -v localhost:8080 -u user:3b95eb26-0348-4ec3-976f-7aa3219a814a`

or using httpie:

`http -v :8080 --auth user:3b95eb26-0348-4ec3-976f-7aa3219a814a`

Request:
```
 GET / HTTP/1.1
 Accept: */*
 Accept-Encoding: gzip, deflate
 Authorization: Basic dXNlcjozYjk1ZWIyNi0wMzQ4LTRlYzMtOTc2Zi03YWEzMjE5YTgxNGE=
 Connection: keep-alive
 Host: localhost:8080
 User-Agent: HTTPie/0.9.4
 ```
 
 
 Response:
 ```
 HTTP/1.1 200 
 Cache-Control: no-cache, no-store, max-age=0, must-revalidate
 Connection: keep-alive
 Content-Type: application/json
 Date: Tue, 04 Feb 2020 18:20:53 GMT
 Expires: 0
 Keep-Alive: timeout=60
 Pragma: no-cache
 Set-Cookie: JSESSIONID=7DD75CAAB1FDA2EEA506A1AAFCA79018; Path=/; HttpOnly
 Transfer-Encoding: chunked
 X-Content-Type-Options: nosniff
 X-Frame-Options: DENY
 X-XSS-Protection: 1; mode=block
 
 [
     "Tom",
     "Jerry"
 ]
```