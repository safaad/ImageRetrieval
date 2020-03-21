from py4j.java_gateway import JavaGateway
gateway = JavaGateway()                   # connect to the JVM


search = gateway.entry_point               # get the AdditionApplication instance
value = search.search("people") # call the addition method
print(*value)