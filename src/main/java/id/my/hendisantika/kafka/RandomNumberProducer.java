package id.my.hendisantika.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Project : kafka-producer-consumer
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Visit: https://s.id/hendisantika
 * Date: 19/05/24
 * Time: 18.17
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RandomNumberProducer {

    private static final int MIN = 10;
    private static final int MAX = 100_000;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
}
