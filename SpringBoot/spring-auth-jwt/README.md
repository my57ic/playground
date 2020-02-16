Usage example

`http -v :8080 X-API-Key:fc3c2224-a71c-46cf-ab21-725c27e76aa5`

Request:
```
GET / HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: localhost:8080
User-Agent: HTTPie/0.9.4
X-API-Key: fc3c2224-a71c-46cf-ab21-725c27e76aa5
 ```
 
 
 Response:
 ```
HTTP/1.1 200 
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Connection: keep-alive
Content-Type: application/json
Date: Sun, 09 Feb 2020 21:10:47 GMT
Expires: 0
Keep-Alive: timeout=60
Pragma: no-cache
Transfer-Encoding: chunked
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block

[
    "Tom",
    "Jerry"
]

```