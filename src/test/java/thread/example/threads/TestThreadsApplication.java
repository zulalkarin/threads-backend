package thread.example.threads;

import com.threadmanager.ThreadManagementApplication;

import org.springframework.boot.SpringApplication;

public class TestThreadsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ThreadManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
