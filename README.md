# Thoth

A simple URL shortner using noir, mongodb and thinked to be deployed to Heroku.
The URLs are shortened using a base62 strings as ids.

## Usage

First go to src/thoth/config.clj and set your environments  for production,
development and test. Basically you need to provide the following:

* An un url with mongo credentials to use.
* An auth-token used to validate requests when creating shortened urls.

After this, you need to set an environment variable to the desired environment:

```bash
export APP_ENV=development
```

```bash
lein deps
lein run
```

### Instructions to deploy to Heroku.

By default, the config file in production enviroment will try to look for a
herokuhq environment variable. So setting up the application to heroku should
be as simple as (assuming you have heroku alredy configured):

1. git clone thoth.
2. cd thoth.
3. heroku create --stack cedar
4. git push heroku master
5. heroku addons:add mongohq:free
6. heroku config:add APP_ENV=production

That should be it! Now your app should be working.

### Thoth API instructions.

## Creating a shortened url

To shorten an url you must do a post method to the url "/shortify" of your app with the following
parameters:

- auth: Containing the auth-token to validate the request.
- url: Url to be shortened.

Example with cURL:

```bash
 curl -i http://myshort.cl/shortify -X url="www.google.com" -X auth="12345678"
```

The response should be a json like these:
```json
{"shortened_url":"1lQ","url":"http://www.google.com"}
```
Where shortened_url is the shortened id.

Then you just need to do a GET  request to your shortener with that id and it
should redirect to the original url:
```bash
 curl -i http://myshort.cl/1lQ
```
## License

Copyright (C) 2012 Rafael Chacón

Distributed under the Eclipse Public License, the same as Clojure.

