all: run

clean:
	rm -f out/PollardHost.jar out/PollardTask.jar

out/PollardHost.jar: out/parcs.jar src/PollardHost.java src/Node.java
	@javac -cp out/parcs.jar src/PollardHost.java
	@jar cf out/PollardHost.jar -C src PollardHost.class
	@rm -f src/PollardHost.class

out/PollardTask.jar: out/parcs.jar src/PollardTask.java src/Node.java
	@javac -cp out/parcs.jar src/PollardTask.java
	@jar cf out/PollardTask.jar -C src PollardTask.class
	@rm -f src/PollardTask.class

build: out/PollardHost.jar out/PollardTask.jar

run: out/PollardHost.jar out/PollardTask.jar
	@cd out && java -cp 'PollardHost.jar:parcs.jar' PollardHost