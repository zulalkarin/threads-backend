package thread.example.threads;

import org.springframework.boot.SpringApplication;

public class TestThreadsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ThreadsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
