require 'sinatra'
require 'cgi'

get '/' do
  'Hello, world'
end

get '/test_endpoint/articles' do
  'Articles'
end

post '/test_endpoint/echo_request_method' do
  request.request_method
end

post '/test_endpoint/awesome_header' do
  env['HTTP_X_SOME_SPECIAL_HEADER']
end
