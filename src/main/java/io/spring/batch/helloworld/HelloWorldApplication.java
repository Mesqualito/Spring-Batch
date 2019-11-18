package io.spring.batch.helloworld;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class HelloWorldApplication {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // Job will run Step, Step will run Tasklet, Tasklet will update JobRepository
    @Bean
    public Step step() {
        return this.stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                        System.out.println("Hello World!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    // Bean 'Job' will be run because Spring Batch is found in the classpath causing the load of
    // a component called the JobLauncherCommandLineRunner.  This component uses the JobLauncher to run
    // any Job-Definitions found in the ApplicationContext
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("job")
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

}
