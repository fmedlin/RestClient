require 'sinatra'
require 'cgi'

get '/' do
  'Hello, world'
end

get '/test_endpoint/articles' do
  'Articles'
end
