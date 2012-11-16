# Active development has stopped

RestClient has been mostly absorbed and reborn in the [twotoasters/hoot]("https://github.com/twotoasters/hoot") library. Since many of my own projects still use RestClient, I'll fix bugs as necessary, but I'll use hoot for new development.

# Running Tests

These instructions are for running the tests in Eclipse. There's no need to run a device or emulator.

1. Install [Robolectric](http://pivotal.github.com/robolectric/).

1. Import these three RestClient projects into Eclipse. Refer to the Robolectric install instructions to fix up any project dependency issues that occur.

  * The RestClient SDK library project in the top level folder.
  * The RestClientExampleApp project in the example-app folder.
  * The RestClientTest project in the example-app/test-app folder.

1. The tests use Sinatra as a local test server.

    gem install sinatra json
    
1. Start the test server by cd'ing to example-app/test-app/sinatra folder and run

    ruby robolectric.rb

1. In Eclipse, select the RestClientTest project and "Run As" => "JUnit Test"



  

