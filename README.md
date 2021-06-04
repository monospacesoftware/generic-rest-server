# Generic Rest Server

A simple REST server that responses with whatever HTTP status you need, and saves the request body or query string to flat files. 

* Simply run `Application`
* The server will start on port 8090 by default.
* It accepts `POST`, `PUT`, `GET`, and `DELETE`.
* The request path should be in the form `/<responseStatus>/<anyPath>`, where responseStatus is the HTTP status code you want to receive back.
* `POST` and `PUT` must have a request body
* `GET` and `DELETE` may optionally send a query string
* The server will save the request body or query string to the `data/` directory, organized by method, then status, then the resource path, and resource name.
* For example, a `POST` with the body `{"foo":"bar"}` to `/200/v2/resource/test` will save the `{"foo":"bar"}` to the file `data/POST/200/v2/resource/test`
* File writing is buffered. The buffer will flush upon shutdown, or call `GET /flush` to force flush the output buffer.
