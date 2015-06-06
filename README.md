# HydraServer

HydraServer is a ServerAPI to create Server-Client Applications.

# Examples

##Server:
```java
public class Main implements ServerListener {
	public static void main(String[] args) {
		Server server = new Server(25565);
		
		server.registerListener(new Main());
		
		server.start();
	}

	@Override
	public void onLogin(Connection con) {
	    System.out.println("new connection");
		con.write("hello");
	}
	
	@Override
	public void onReceive(Connection con, String message) {
		System.out.println("new message from " + con.getClient().getRemoteSocketAddress() + " : " + message);
	}
}
```

##Client:
```java
public class Main implements ClientListener {
	public static void main(String[] args) {
		Client client = new Client("127.0.0.1", 25565);
		
		client.registerListener(new Main());
		
		client.start();
	}

	@Override
	public void onReceive(String message) {
		System.out.println("message from server : " + message);
	}

	@Override
	public void onConnectionLost() {
		System.out.println("lost connection");
	}
}
```

##Explanation


######Classes
Currently there are two server classes:

| Class                          | Explanation                                                           |
| ------------------------------ | --------------------------------------------------------------------- |
| Server                         | the main server class which starts the server                         |
| Connection                     | the connection class handles the connections of the server            |

and one client class:

| Class                          | Explanation                                                           |
| ------------------------------ | --------------------------------------------------------------------- |
| Client                         | the main client class which establishes a connection                  |

##License

Copyright 2015 Marcel Franzen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
