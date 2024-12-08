package thread.example.threads;

import com.threadmanager.ThreadManagementApplication;

import org.springframework.boot.SpringApplication;

public class TestThreadsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ThreadManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}




/*
 * package thread.example.threads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestThreadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestThreadsApplication.class, args);
	}

}

 */
