require 'sinatra'
require 'cgi'
require 'json'

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

patch '/test_endpoint/should_patch' do
  halt 400 unless request.patch?
end

post '/test_endpoint/should_post_form' do
  halt 400 unless
    params[:username] == 'assad' &&
    params[:password] == '12345'
end

post '/test_endpoint/should_post_data' do
  data = JSON.parse request.body.read
  halt 400 unless data["id"] == 42
end
