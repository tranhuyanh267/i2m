package common.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.MessageChannel;

public interface UsernameProcessor {

    String INPUT = "destIn";
    String OUTPUT = "destOut";

    @Input(INPUT)
    PollableMessageSource destIn();

    @Output(OUTPUT)
    MessageChannel destOut();
}
